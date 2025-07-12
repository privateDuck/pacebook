package com.groupd.pacebook.service;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FriendService {

    //private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FriendService(UserRepository userRepository, UserService userService) {
        //this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Check if either has sent a request to the other
    public boolean friendRequestExists(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow();
        User user2 = userRepository.findById(userId2).orElseThrow();

        return user1.getSentRequests().contains(user2) || user2.getSentRequests().contains(user1);
    }

    // Check if either a request is sent/received or already a friend
    public boolean connectionExists(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow();
        User user2 = userRepository.findById(userId2).orElseThrow();

        return user1.getFriends().contains(user2)
                || user1.getSentRequests().contains(user2)
                || user2.getSentRequests().contains(user1);
    }

    // Send friend request method (you probably already have this)
    public boolean sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            return false;
        }
        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> receiverOpt = userRepository.findById(receiverId);
        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return false;
        }
        User sender = senderOpt.get();
        User receiver = receiverOpt.get();

        if (connectionExists(senderId, receiverId)) {
            return false;
        }

        if (sender.getFriends().contains(receiver)) {
            return false;
        }

        sender.getSentRequests().add(receiver);
        return true;
    }

    public List<User> getRequestingUsers(long senderId) {
        User user = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new ArrayList<>(user.getReceivedRequests());
    }

    public List<User> getRequestedUsers(long senderId) {
        User user = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new ArrayList<>(user.getSentRequests());
    }

    // Accept friend request: set status ACCEPTED and add friends both sides
    public void acceptFriendRequest(Long receiverId, Long senderId) {

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("You can't accept your own request.");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        // Check if the request exists
        if (!receiver.getReceivedRequests().contains(sender)) {
            throw new IllegalStateException("No friend request from this user.");
        }

        // Remove friend request
        receiver.getReceivedRequests().remove(sender);
        sender.getSentRequests().remove(receiver);

        // Add to friends (both directions)
        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        // Save both
        userRepository.save(receiver);
        userRepository.save(sender);
    }

    // Decline friend request: set status DECLINED
    public void declineFriendRequest(Long receiverId, Long senderId) {

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("You can't accept your own request.");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        // Check if the request exists
        if (!receiver.getReceivedRequests().contains(sender)) {
            throw new IllegalStateException("No friend request from this user.");
        }

        // Remove friend request
        receiver.getReceivedRequests().remove(sender);
        sender.getSentRequests().remove(receiver);

        // Save both
        userRepository.save(receiver);
        userRepository.save(sender);
    }
}
