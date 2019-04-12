ALTER TABLE developers
ADD salary INT NOT NULL ;

SELECT *
FROM projects
INNER JOIN project_developer ON projects.id = project_developer.id_project
INNER JOIN developers ON project_developer.id_developer = developers.id
GROUP BY projects.id
ORDER BY sumSalary DESC
LIMIT 1;

SELECT sum(salary)
FROM developers
INNER JOIN skills ON developers.developer_id = skills.developer_id
WHERE skills.industry = "JAVA";

ALTER TABLE projects
ADD cost  INT NOT NULL ;

SELECT p.name
FROM projects p
WHERE p.cost = (SELECT MIN(cost) FROM projects);

SELECT AVG(d.salary)
FROM developers d, project_developer pd, projects p
WHERE d.id = pd.id_developer AND p.id = pd.id_project AND p.cost = (SELECT MIN(cost) FROM projects);
