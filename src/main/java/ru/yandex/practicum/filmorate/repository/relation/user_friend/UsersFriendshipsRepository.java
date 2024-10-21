package ru.yandex.practicum.filmorate.repository.relation.user_friend;

import ru.yandex.practicum.filmorate.repository.relation.BaseRelationRepository;

import java.util.List;

public interface UsersFriendshipsRepository extends BaseRelationRepository {
    List<Long> findCommonFriends(long userId, long otherUserId);
}
