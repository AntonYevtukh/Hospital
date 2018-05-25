package services;

import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import model.dao.interfaces.DaoFactory;
import model.dao.interfaces.UserDao;
import model.database.TransactionManager;
import model.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static services.UserService.getUsersForPage;

@PrepareForTest({UserDao.class, DaoFactory.class, TransactionManager.class})
public class UserServiceTest {

    UserDao userDao = mock(UserDao.class);


    @Before
    public void setUpTest() throws Exception {
        User user = new User(1);
        user.setLogin("anton");
        user.setPassword("5c9f9903627f3b3b55d776a055189e44");
        when(userDao.selectByLogin("anton")).thenReturn(user);
        when(userDao.selectByLogin("vasya")).thenThrow(EntityNotFoundException.class);
        when(userDao.selectCountOfUsers()).thenReturn(100L);
        Whitebox.setInternalState(UserService.class, "userDao", userDao);
    }

    @Test
    public void signInTestSuccess() {
        assertEquals(UserService.signIn("anton", "aa11"), 1);
    }

    @Test(expected = ErrorMessageKeysContainedException.class)
    public void signInTestLoginFail() {
        UserService.signIn("vasya", "aa11");
    }

    @Test(expected = ErrorMessageKeysContainedException.class)
    public void signInTestPasswordFail() {
        UserService.signIn("anton", "aa22");
    }

    @Test
    public void testCountOfUsersPages() {
        assertEquals(getUsersForPage(1, 10).getTotalPages(), 10);
    }
}
