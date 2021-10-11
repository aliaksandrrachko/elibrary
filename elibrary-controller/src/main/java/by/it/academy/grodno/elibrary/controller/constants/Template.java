package by.it.academy.grodno.elibrary.controller.constants;

public final class Template {
    private Template() { }

    public static final class Error{
        private Error(){}

        public static final String TEMPLATE_NAME = "error";
        public static final String ERROR_TEXT = "error";
        public static final String TIMESTAMP = "timestamp";
        public static final String PATH = "rpath";
        public static final String STATUS = "status";
        public static final String MESSAGE = "message";
        public static final String EXCEPTION = "exception";
        public static final String TRACE = "trace";
        public static final String CURRENT_USER = "currentUser";
    }
}
