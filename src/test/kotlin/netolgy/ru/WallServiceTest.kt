package netolgy.ru

import org.junit.Assert.*
import org.junit.Before
import kotlin.test.Test

class WallServiceTest {
    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun newIdEmpty() {
        val posts = WallService
        val result = posts.newId()

        assertEquals(1, result)
    }


    @Test
    fun newIdNotEmpty() {
        val posts = WallService
        posts.add(Post(1, 1, 33333333, "Первая запись"))
        val result = posts.newId()

        assertEquals(2, result)
    }

    @Test
    fun updateTrue() {
        val posts = WallService
        posts.add(Post(1, 1, 33333333, "Первая запись"))
        val newPost = Post(1, 1, 33333333, "Первая запись", id = 1)
        val result = posts.update(newPost)

        assertEquals(true, result)
    }

    @Test
    fun updateFalse() {
        val posts = WallService
        posts.add(Post(1, 1, 33333333, "Первая запись"))
        val newPost = Post(1, 1, 33333333, "Первая запись", id = 3)
        val result = posts.update(newPost)

        assertEquals(false, result)
    }


}