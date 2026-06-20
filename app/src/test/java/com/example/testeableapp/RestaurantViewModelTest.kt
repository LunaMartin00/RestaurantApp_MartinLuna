package com.example.testeableapp

import com.example.testeableapp.model.MenuData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RestaurantViewModelTest {

    private lateinit var viewModel: RestaurantViewModel

    @Before
    fun setup() {
        viewModel = RestaurantViewModel()
    }

    @Test
    fun `addItem adds item to quantities map`() {
        val itemId = 1
        viewModel.addItem(itemId)
        
        val quantities = viewModel.quantities.value
        assertEquals(1, quantities[itemId])
    }

    @Test
    fun `incrementItem increases quantity of an existing item`() {
        val itemId = 1
        viewModel.addItem(itemId)
        viewModel.incrementItem(itemId)
        
        val quantities = viewModel.quantities.value
        assertEquals(2, quantities[itemId])
    }

    @Test
    fun `decrementItem decreases quantity of an item`() {
        val itemId = 1
        viewModel.addItem(itemId)
        viewModel.addItem(itemId) // Qty = 2
        viewModel.decrementItem(itemId)
        
        val quantities = viewModel.quantities.value
        assertEquals(1, quantities[itemId])
    }

    @Test
    fun `decrementItem removes item when quantity reaches zero`() {
        val itemId = 1
        viewModel.addItem(itemId)
        viewModel.decrementItem(itemId)
        
        val quantities = viewModel.quantities.value
        assertTrue("El ítem debería haber sido eliminado", !quantities.containsKey(itemId))
    }

    @Test
    fun `total calculation is correct for multiple items`() {
        val item1 = MenuData.items[0]
        val item2 = MenuData.items[1]
        
        viewModel.addItem(item1.id)
        viewModel.addItem(item2.id)
        viewModel.addItem(item2.id)


        assertEquals(17.50, viewModel.total.value, 0.01)
    }

    @Test
    fun `placeOrder generates confirmation when order is not empty`() {
        val item = MenuData.items[0]
        viewModel.addItem(item.id)
        viewModel.placeOrder()
        
        val confirmation = viewModel.confirmation.value
        assertNotNull(confirmation)
        assertEquals(1, confirmation?.itemCount)
        assertEquals(item.price, confirmation?.total ?: 0.0, 0.01)
    }

    @Test
    fun `dismissConfirmation clears order and resets confirmation`() {
        viewModel.addItem(1)
        viewModel.placeOrder()
        viewModel.dismissConfirmation()
        
        assertTrue(viewModel.quantities.value.isEmpty())
        assertNull(viewModel.confirmation.value)
    }
}
