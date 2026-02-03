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

    @Test
    fun addComment() {
        val posts = WallService
        posts.add(Post(1, 1, 33333333, "Первая запись"))
        val newComment = Comment(1, 1, 124551241,"Комментарий к первой записи")
        val result = posts.createComment(newComment.idPost, newComment)

        assertEquals(1, result.id)
    }
    

    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        val posts = WallService
        posts.add(Post(1, 1, 33333333, "Первая запись"))
        val newComment = Comment(1, 3, 124551241,"Комментарий к первой записи")
        val comment = posts.createComment(newComment.idPost, newComment)
    }
}