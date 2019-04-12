SELECT *
FROM developers
INNER JOIN skill_developer ON developers.id = skill_developer.id_developer
INNER JOIN skills ON skill_developer.id_skill = skills.id
WHERE skills.industry = ?;
