package netolgy.ru

data class Comment(
    val count: Int = 0,             // Количество комментариев
    val canPost: Boolean = true,       // Может ли текущий пользователь комментировать
    val groupsCanPost: Boolean = false, // Могут ли сообщества комментировать
    val canClose: Boolean = true,      // Может ли текущий пользователь закрыть комментарии к записи
    val canOpen: Boolean = true        // Может ли текущий пользователь открыть комментарии к записи
)

data class Like(
    val count: Int = 0,             // Число пользователей которым понравилась запись
    val userLikes: Boolean = false,     // Наличие отметки "Мне нравится" у текущего пользователя
    val canLike: Boolean = false,       // Может ли текущий пользователь лайкнуть
    val canPublished: Boolean = false  // Может ли текущий пользователь сделать репост
)

data class Post(
    val ownerId: Int,           // Идентификатор владельца стены на которой размещена запись
    val fromId: Int,            // Идентификатор автора записи
    val date: Int,              // Время юникстайм
    val text: String,           // Текст записи
    val id: Int = 0,                // Идентификатор записи
    val createdBy: Int = 0,         // Идентификатор администратора опубликовавшего запись (только для сообществ)
    val replyOwnerId: Int = 0,      // Идентификатор владельца записи в ответ на которую оставлена текущая запись
    val replyPostId: Int = 0 ,       // Идентификатор записи ответом на которую является текущая запись
    val friendsOnly: Boolean = false,   // Только для друзей
    val comments: Comment = Comment(),              // Комментарии к записи
    val likes: Like = Like(),                    // Информация о лайках к записи
    val postType: String = "post",      // Тип записи (post, copy, reply, postpone, suggest)
    val canPin: Boolean = true,         // Может ли текущий пользователь закрепить запись
    val canDelete: Boolean = true,      // Может ли текущий пользователь удалить запись
    val canEdit: Boolean = true,        // Может ли текущий пользователь редактировать запись
    val isPined: Boolean = false,       // Запись закреплена
    val markedAsAds: Boolean = false,   // Является ли запись рекламой
    val isFavorite: Boolean = false,    // Истина если запись добавлена в закладки у Пользователя
    val postOpenedId: Int = 0           // Идентификатор отложенной записи
)

object WallService {
    private var posts = emptyArray<Post>()

    fun clear() {
        posts = emptyArray()
        // также здесь нужно сбросить счетчик для id постов, если он у вас используется
    }

    fun add(post: Post) {
        val newPost = if (post.id == 0) post.copy(id = newId()) else post.copy()
        posts += newPost
    }

    fun newId(): Int {
        var maxId = 0
        for (post in posts) if (post.id > maxId) maxId = post.id
        return maxId + 1
    }

    fun gefById(id: Int): Post {
        for (post in posts) {
            if (post.id == id) {
                return post
            }
        }
        return posts.last() // Пока не знаю как в котлине вернуть пустой объект или Null
    }

    fun changeTextById(id: Int, text: String): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                val exchangedPost = post.copy(text = text)
                posts[index] = exchangedPost
                return true
            }
        }
        return false
    }

    fun update(post: Post): Boolean {
        for ((index, currentPost) in posts.withIndex()) {
            if (currentPost.id == post.id) {
                posts[index] = post
                return true
            }
        }
        return false
    }

}

fun main() {
    val posts = WallService

    posts.add(Post(1, 1, 33333333, "Первая запись"))
    posts.add(Post(1, 1, 33333356, "Вторая запись"))
    posts.add(Post(1, 1, 33339456, "Третья запись"))

    if (posts.changeTextById(2, "Вторая запись исправлена")) {
        val post = posts.gefById(2)
        println(post)
    } else println("Ошибка изменения текста записи")

    val exchangedPost = Post(2, 2, 33339999, "Новая третья запись", id = 3)
    if (posts.update(exchangedPost)){
        val post = posts.gefById(3)
        println(post)
    } else println("Ошибка изменения записи")

}