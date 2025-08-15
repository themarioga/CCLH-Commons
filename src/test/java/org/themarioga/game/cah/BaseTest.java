package org.themarioga.game.cah;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@SpringBootTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@Transactional
@Rollback
public class BaseTest {

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void beforeTests() {
        entityManager.clear();

        entityManager.createNativeQuery("CREATE TABLE t_configuration(conf_key TEXT NOT NULL, conf_value TEXT NOT NULL, PRIMARY KEY (conf_key))").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('default_language', 'es')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_default_game_type', '1')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_default_game_punctuation_type', '1')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_default_game_length', '1')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_min_number_of_players', '3')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_max_number_of_players', '9')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_default_dictionary_id', '00000000-0000-0000-0000-000000000000')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO t_configuration(conf_key, conf_value) VALUES ('game_default_number_cards_in_hand', '5')").executeUpdate();
    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

}
