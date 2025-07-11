package com.groupd.pacebook.repository;

import com.groupd.pacebook.model.FriendRequest;
import com.groupd.pacebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Find all friend requests received by a user with a specific status
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.Status status);

    // Find all friend requests sent by a user with a specific status
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequest.Status status);

    // Find a friend request by sender and receiver (any status)
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    // Find friend requests by receiver regardless of status (to show all incoming requests)
    List<FriendRequest> findByReceiver(User receiver);

    // Optional: Find friend requests by sender regardless of status
    List<FriendRequest> findBySender(User sender);

    // Custom query to find friend requests between two users with status in a given set (PENDING or ACCEPTED)
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.sender = :sender AND fr.receiver = :receiver OR fr.sender = :receiver AND fr.receiver = :sender) " +
            "AND fr.status IN :statuses")
    List<FriendRequest> findBetweenUsersWithStatuses(@Param("sender") User sender,
                                                     @Param("receiver") User receiver,
                                                     @Param("statuses") List<FriendRequest.Status> statuses);
}
