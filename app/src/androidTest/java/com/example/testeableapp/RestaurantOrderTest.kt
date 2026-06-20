package com.example.testeableapp

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.testeableapp.model.MenuData
import org.junit.Rule
import org.junit.Test

class RestaurantOrderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun emptyOrderMessage_isVisibleAtStart() {
        composeTestRule.onNodeWithTag("emptyOrderMessage").assertIsDisplayed()
    }

    @Test
    fun allMenuItems_areVisible() {
        MenuData.items.forEach { item ->
            composeTestRule.onNodeWithTag("menuItem_${item.id}").assertIsDisplayed()
            composeTestRule.onNodeWithTag("menuItemName_${item.id}").assertIsDisplayed()
        }
    }

    @Test
    fun totalUpdates_whenAddingItem() {
        val item = MenuData.items[0]
        
        composeTestRule.onNodeWithTag("addButton_${item.id}").performClick()

        composeTestRule.onNodeWithTag("totalValue").assertTextContains("%.2f".format(item.price))
    }

    @Test
    fun placeOrderButton_showsConfirmationDialog() {
        val item = MenuData.items[0]
        composeTestRule.onNodeWithTag("addButton_${item.id}").performClick()
        
        composeTestRule.onNodeWithTag("placeOrderButton").performClick()
        
        composeTestRule.onNodeWithTag("confirmationDialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("confirmationTitle").assertIsDisplayed()
    }

    @Test
    fun confirmationOkButton_clearsOrder() {
        val item = MenuData.items[0]
        composeTestRule.onNodeWithTag("addButton_${item.id}").performClick()
        composeTestRule.onNodeWithTag("placeOrderButton").performClick()
        
        composeTestRule.onNodeWithTag("confirmationOkButton").performClick()
        
        composeTestRule.onNodeWithTag("emptyOrderMessage").assertIsDisplayed()
    }
}

/**
 * Extensión para facilitar la comprobación de texto dentro de un nodo por tag.
 */
fun androidx.compose.ui.test.SemanticsNodeInteraction.assertTextContains(text: String) {
    this.assert(hasText(text, substring = true))
}
