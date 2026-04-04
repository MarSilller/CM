package cm_a15316.aiapplication

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import cm_a15316.aiapplication.domain.GameEngine
import cm_a15316.aiapplication.domain.models.Card
import cm_a15316.aiapplication.domain.models.Deck
import cm_a15316.aiapplication.domain.models.Rank
import cm_a15316.aiapplication.domain.models.Suit
import cm_a15316.aiapplication.ui.GameViewModel
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameInstrumentedTest {

    @After
    fun tearDown() {
        GameViewModel.testEngine = null
    }

    private fun launchWithDeck(cards: List<Card>): ActivityScenario<MainActivity> {
        val deck = Deck(1)
        deck.cards.clear()
        deck.cards.addAll(cards.reversed())
        GameViewModel.testEngine = GameEngine(deck)
        return ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testBettingAndDealingFlow() {
        val scenario = launchWithDeck(listOf(
            Card(Suit.SPADES, Rank.TWO), Card(Suit.HEARTS, Rank.THREE),
            Card(Suit.DIAMONDS, Rank.FOUR), Card(Suit.CLUBS, Rank.FIVE)
        ))

        onView(withId(R.id.btn_bet_10)).check(matches(isEnabled()))
        onView(withId(R.id.btn_deal)).check(matches(not(isEnabled())))

        onView(withId(R.id.btn_bet_10)).perform(click())
        onView(withId(R.id.tv_current_bet)).check(matches(withText("Bet: $10")))
        onView(withId(R.id.btn_deal)).check(matches(isEnabled()))

        onView(withId(R.id.btn_deal)).perform(click())

        onView(withId(R.id.btn_bet_10)).check(matches(not(isEnabled())))
        onView(withId(R.id.btn_deal)).check(matches(not(isEnabled())))
        onView(withId(R.id.btn_hit)).check(matches(isEnabled()))
        onView(withId(R.id.btn_stand)).check(matches(isEnabled()))
        
        scenario.close()
    }

    @Test
    fun testHitAndBust() {
        val scenario = launchWithDeck(listOf(
            Card(Suit.SPADES, Rank.TEN), Card(Suit.HEARTS, Rank.SIX),
            Card(Suit.DIAMONDS, Rank.FOUR), Card(Suit.CLUBS, Rank.FIVE),
            Card(Suit.SPADES, Rank.TEN)
        ))

        onView(withId(R.id.btn_bet_10)).perform(click())
        onView(withId(R.id.btn_deal)).perform(click())
        
        onView(withId(R.id.btn_hit)).perform(click())

        onView(withId(R.id.tv_game_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_game_result)).check(matches(withText("Dealer Wins!")))
        onView(withId(R.id.btn_deal)).check(matches(withText("Next Hand")))
        
        scenario.close()
    }

    @Test
    fun testPlayerBlackjack() {
        val scenario = launchWithDeck(listOf(
            Card(Suit.SPADES, Rank.ACE), Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.DIAMONDS, Rank.FOUR), Card(Suit.CLUBS, Rank.FIVE)
        ))

        onView(withId(R.id.btn_bet_10)).perform(click())
        onView(withId(R.id.btn_deal)).perform(click())

        onView(withId(R.id.tv_game_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_game_result)).check(matches(withText("Blackjack!")))
        
        scenario.close()
    }

    @Test
    fun testStandAndPush() {
        val scenario = launchWithDeck(listOf(
            Card(Suit.SPADES, Rank.TEN), Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.DIAMONDS, Rank.TEN), Card(Suit.CLUBS, Rank.TEN)
        ))

        onView(withId(R.id.btn_bet_10)).perform(click())
        onView(withId(R.id.btn_deal)).perform(click())
        onView(withId(R.id.btn_stand)).perform(click())

        onView(withId(R.id.tv_game_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_game_result)).check(matches(withText("Push!")))
        onView(withId(R.id.btn_deal)).check(matches(withText("Next Hand")))
        
        scenario.close()
    }
    
    @Test
    fun testReshuffleThreshold() {
        val scenario = launchWithDeck(listOf(
            Card(Suit.SPADES, Rank.TWO), Card(Suit.HEARTS, Rank.THREE),
            Card(Suit.DIAMONDS, Rank.FOUR), Card(Suit.CLUBS, Rank.FIVE)
        ))
        
        onView(withId(R.id.btn_bet_10)).perform(click())
        onView(withId(R.id.btn_deal)).perform(click())
        
        onView(withId(R.id.btn_deal)).check(matches(not(isEnabled())))
        onView(withId(R.id.tv_current_bet)).check(matches(isDisplayed()))
        
        scenario.close()
    }
}
