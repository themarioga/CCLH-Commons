-- v1.1.0_1 - Collaborators

INSERT INTO t_dictionary_collaborators(dictionary_id, user_id, accepted, can_edit) SELECT id, creator_id, true, true FROM t_dictionary;