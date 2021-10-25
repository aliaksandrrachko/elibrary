-- Recursiver query
with recursive r as (
	select c.id, c.parent_id, c.category_name 
	from category c
	where id = 1
	
	union 
	
	select c1.id, c1.parent_id, c1.category_name 
	from category c1
		join r
			on c1.parent_id = r.id
)

select * from r;

