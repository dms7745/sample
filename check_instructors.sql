SELECT COUNT(*) as total_instructors 
FROM site_user 
WHERE role = 'INSTRUCTOR' AND approved = true;
