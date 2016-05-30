package com.twu.biblioteca.itemlisters;

import java.util.ArrayList;
import java.util.Arrays;
import com.twu.biblioteca.user.User;
import com.twu.biblioteca.items.Item;
import com.twu.biblioteca.items.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.contrib.java.lang.system.*;
import static org.mockito.Mockito.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

public class MovieListerTest {

    private User user;
    private Movie m1;
    private Movie m2;
    private ArrayList<Item> movieList;
    private MovieLister movieLister;

    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Before
    public void beforeEach() {
        user = mock(User.class);
        m1 = mock(Movie.class);
        m2 = mock(Movie.class);
        movieList = new ArrayList<Item>();
        movieList.addAll(Arrays.asList(m1, m2));
        movieLister = new MovieLister(movieList);
    }

    @Test
    public void listItemsShouldPrintAllItemsInListerThatAreNotCheckedOut() {
        when(m1.isCheckedOut()).thenReturn(false);
        when(m2.isCheckedOut()).thenReturn(true);
        movieLister.listItems();
        verify(m1, times(1)).printDetails(System.out);
        verify(m2, never()).printDetails(System.out);
    }

    @Test
    public void checkOutShouldPrintASuccessMessageIfYouCheckedOutSuccessfully() {
        systemInMock.provideLines("The Godfather", "Francis Ford Coppola", "1972");
        when(m1.isCheckedOut()).thenReturn(false);
        when(m1.isEqualTo(any(Item.class))).thenReturn(true);
        when(m1.checkOut(any(User.class))).thenReturn("Thank you! Enjoy the movie");
        movieLister.checkOut(user);
        assertTrue(systemOutRule.getLog().contains("Thank you! Enjoy the movie"));
    }

    @Test
    public void checkOutShouldPrintAFailureMessageIfYouCouldNotCheckOut() {
        systemInMock.provideLines("The Godfather", "Francis Ford Coppola", "1972");
        movieLister.checkOut(user);
        assertTrue(systemOutRule.getLog().contains("That item is not available."));
    }

    @Test
    public void giveBackShouldPrintASuccessMessageIfYouReturnedSuccessfully() {
        systemInMock.provideLines("The Godfather", "Francis Ford Coppola", "1972");
        when(m1.isCheckedOut()).thenReturn(true);
        when(m1.isEqualTo(any(Item.class))).thenReturn(true);
        when(m1.giveBack(any(User.class))).thenReturn("Thank you for returning the movie.");
        movieLister.giveBack(user);
        assertTrue(systemOutRule.getLog().contains("Thank you for returning the movie."));
    }

    @Test
    public void giveBackShouldPrintAFailureMessageIfYouCouldNotReturn() {
        systemInMock.provideLines("The Godfather", "Francis Ford Coppola", "1972");
        movieLister.giveBack(user);
        assertTrue(systemOutRule.getLog().contains("That is not a valid item to return."));
    }
}
