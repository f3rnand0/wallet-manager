MERGE INTO user KEY(user_id) VALUES (1, 'Fernando', 'Guerra', 'fguerra');
MERGE INTO user KEY(user_id) VALUES (2, 'John', 'Doe', 'jdoe');

MERGE INTO account KEY(user_user_id) VALUES (1234567890, 1);
MERGE INTO account KEY(user_user_id) VALUES (1234567891, 2);