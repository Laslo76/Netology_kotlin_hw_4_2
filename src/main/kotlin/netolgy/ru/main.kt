package netolgy.ru

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
    val copyHistory: Post? = null,      // Первоисточник записи. Возвращается только если запись является репостом
    val canPin: Boolean = true,         // Может ли текущий пользователь закрепить запись
    val canDelete: Boolean = true,      // Может ли текущий пользователь удалить запись
    val canEdit: Boolean = true,        // Может ли текущий пользователь редактировать запись
    val isPined: Boolean = false,       // Запись закреплена
    val markedAsAds: Boolean = false,   // Является ли запись рекламой
    val isFavorite: Boolean = false,    // Истина если запись добавлена в закладки у Пользователя
    val postOpenedId: Int = 0,          // Идентификатор отложенной записи
    val attachments: Array<Attachment?> = emptyArray()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (ownerId != other.ownerId) return false
        if (fromId != other.fromId) return false
        if (date != other.date) return false
        if (id != other.id) return false
        if (createdBy != other.createdBy) return false
        if (replyOwnerId != other.replyOwnerId) return false
        if (replyPostId != other.replyPostId) return false
        if (friendsOnly != other.friendsOnly) return false
        if (canPin != other.canPin) return false
        if (canDelete != other.canDelete) return false
        if (canEdit != other.canEdit) return false
        if (isPined != other.isPined) return false
        if (markedAsAds != other.markedAsAds) return false
        if (isFavorite != other.isFavorite) return false
        if (postOpenedId != other.postOpenedId) return false
        if (text != other.text) return false
        if (comments != other.comments) return false
        if (likes != other.likes) return false
        if (postType != other.postType) return false
        if (copyHistory != other.copyHistory) return false
        if (!attachments.contentEquals(other.attachments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ownerId
        result = 31 * result + fromId
        result = 31 * result + date
        result = 31 * result + id
        result = 31 * result + createdBy
        result = 31 * result + replyOwnerId
        result = 31 * result + replyPostId
        result = 31 * result + friendsOnly.hashCode()
        result = 31 * result + canPin.hashCode()
        result = 31 * result + canDelete.hashCode()
        result = 31 * result + canEdit.hashCode()
        result = 31 * result + isPined.hashCode()
        result = 31 * result + markedAsAds.hashCode()
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + postOpenedId
        result = 31 * result + text.hashCode()
        result = 31 * result + comments.hashCode()
        result = 31 * result + likes.hashCode()
        result = 31 * result + postType.hashCode()
        result = 31 * result + (copyHistory?.hashCode() ?: 0)
        result = 31 * result + attachments.contentHashCode()
        return result
    }
}

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

    fun getById(id: Int): Int {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                return index
            }
        }
        return -1
    }

    fun changeTextById(id: Int, text: String): Boolean {
        val indexDesiredPost = getById(id)
        if (indexDesiredPost >= 0) {
            val exchangedPost = posts[indexDesiredPost].copy(text = text)
            posts[indexDesiredPost] = exchangedPost
            return true
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

interface Attachment {
    val type: String
}


data class Photo(
    val id: Int,
    val ownerId: Int,
    val userId: Int,
    val URL: String,
    val date: Int,
    val text: String,
    val thumbHash: String,
    val width: Int,
    val height: Int,
    )


class PhotoAttachment(
    override val type: String,
    val photo: Photo
) : Attachment {

}


class AudioAttachment(
    override val type: String,
    val audio: Audio
) : Attachment {

}


data class Audio(
    val id: Int,
    val ownerId: Int,
    val URL: String,
    val date: Int,
    val artist: String,
    val title: String,
    val duration: Int
)


class AttachmentVideo(
    override val type: String,
    val video: Video

) : Attachment {
}


data class Video(
    val id: Int,
    val ownerId: Int,
    val description: String,
    val duration: Int,
    val URL: String,
    val date: Int,
    )


class AttachmentFile(
    override val type: String,
    val file: File,
) : Attachment {
}


data class File(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val URL: String,
    val date: Int,
    val size: Int
)


class AttachmentHistory(
    override val type: String,
    val history: History
): Attachment {
}


data class History(
    val id: Int,
    val ownerId: Int,
    val typeHistory: String,
    val photo: Photo?,
    val video: Video?
)


fun main() {
    val posts = WallService

    posts.add(Post(1, 1, 33333333, "Первая запись"))
    posts.add(Post(1, 1, 33333356, "Вторая запись"))
    posts.add(Post(1, 1, 33339456, "Третья запись"))

    if (!posts.changeTextById(2, "Вторая запись исправлена")) println("Ошибка изменения текста записи")

    val exchangedPost = Post(2, 2, 33339999, "Новая третья запись", id = 3)
    if (!posts.update(exchangedPost)) println("Ошибка изменения записи")

}