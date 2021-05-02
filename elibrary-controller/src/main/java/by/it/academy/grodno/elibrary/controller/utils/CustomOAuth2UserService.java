package by.it.academy.grodno.elibrary.controller.utils;

import by.it.academy.grodno.elibrary.api.dto.users.socialresponses.GitHubUserResponse;
import by.it.academy.grodno.elibrary.api.dto.users.socialresponses.FacebookUserResponse;
import by.it.academy.grodno.elibrary.api.mappers.FacebookUserResponseToUserMapper;
import by.it.academy.grodno.elibrary.api.mappers.GitHubUserResponseToUserMapper;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.entities.users.Role;
import by.it.academy.grodno.elibrary.entities.users.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.util.*;

import static java.util.Objects.requireNonNull;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";

    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
            new ParameterizedTypeReference<Map<String, Object>>() {};

    private final IUserService userService;

    private final RestOperations restOperations;

    private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();

    public CustomOAuth2UserService(IUserService userService) {
        this.userService = requireNonNull(userService);
        this.restOperations = createRestTemplate();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils
                .hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE,
                    "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
                            + userRequest.getClientRegistration().getRegistrationId(),
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        if (!StringUtils.hasText(userNameAttributeName)) {
            OAuth2Error oauth2Error = new OAuth2Error(
                    MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
                    "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: "
                            + registrationId,
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
        ResponseEntity<Map<String, Object>> response = getResponse(userRequest, request);
        Map<String, Object> userAttributes = response.getBody();
        User user = findUserFromDataBaseOrCreate(userAttributes, registrationId);
        return new DefaultOAuth2User(user.getAuthorities(), userToMap(user), "id");
    }

    private ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
        try {
            return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        } catch (OAuth2AuthorizationException ex) {
            OAuth2Error oauth2Error = ex.getError();
            StringBuilder errorDetails = new StringBuilder();
            errorDetails.append("Error details: [");
            errorDetails.append("UserInfo Uri: ")
                    .append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
            errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
            if (oauth2Error.getDescription() != null) {
                errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
            }
            errorDetails.append("]");
            oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(),
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        } catch (UnknownContentTypeException ex) {
            String errorMessage = "An error occurred while attempting to retrieve the UserInfo Resource from '"
                    + userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()
                    + "': response contains invalid content type '" + ex.getContentType().toString() + "'. "
                    + "The UserInfo Response should return a JSON object (content type 'application/json') "
                    + "that contains a collection of name and value pairs of the claims about the authenticated End-User. "
                    + "Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '"
                    + userRequest.getClientRegistration().getRegistrationId() + "' conforms to the UserInfo Endpoint, "
                    + "as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, errorMessage, null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        } catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }
    }

    private static final Class<FacebookUserResponse> FACEBOOK_RESPONSE_CLASS = FacebookUserResponse.class;

    private static final Class<GitHubUserResponse> GIT_HUB_RESPONSE_CLASS = GitHubUserResponse.class;

    private Class<?> getResponseClassByRegistrationId(String registrationId){
        if (registrationId.equals("facebook")){
            return FACEBOOK_RESPONSE_CLASS;
        } else if (registrationId.equals("github")) {
            return  GIT_HUB_RESPONSE_CLASS;
        }
        return null;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FacebookUserResponseToUserMapper facebookUserResponseToUserMapper;

    @Autowired
    private GitHubUserResponseToUserMapper gitHubUserResponseToUserMapper;

    private User findUserFromDataBaseOrCreate(Map<String, Object> attributes, String registrationId) {
        Object responseObject = objectMapper.convertValue(attributes, getResponseClassByRegistrationId(registrationId));
        User user = null;
        if (responseObject instanceof FacebookUserResponse){
            FacebookUserResponse facebookResponse = ((FacebookUserResponse) responseObject);
            user = facebookUserResponseToUserMapper.toEntity(facebookResponse);
            user.setRoles(Collections.singleton(new Role("ROLE_USER_FACEBOOK")));
        } else if (responseObject instanceof GitHubUserResponse) {
            GitHubUserResponse gitHubUserResponse = ((GitHubUserResponse) responseObject);
            user = gitHubUserResponseToUserMapper.toEntity(gitHubUserResponse);
            user.setRoles(Collections.singleton(new Role("ROLE_USER_GITHUB")));
        }

        if (user != null) {
            Optional<User> userOptionalFromDb = userService.findByEmailOrSocialId(user.getEmail(), user.getSocialId());
            if (userOptionalFromDb.isPresent()){
                return userOptionalFromDb.get();
            }
        }
        return userService.createUserFromSocialNetwork(user).orElseThrow(NoSuchElementException::new);
    }

    private static final TypeReference<HashMap<String,Object>> TYPE_REFERENCE_HASH_MAP
            = new TypeReference<HashMap<String,Object>>() {};

    private Map<String, Object> userToMap(User user){
        return objectMapper.convertValue(user, TYPE_REFERENCE_HASH_MAP);
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        return restTemplate;
    }
}