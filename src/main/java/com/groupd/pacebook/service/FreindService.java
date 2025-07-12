package com.groupd.pacebook.service;

import com.groupd.pacebook.model.FriendRequest;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.FriendRequestRepository;
import com.groupd.pacebook.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
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

        boolean alreadyRequested = friendRequestRepository
                .findBySenderAndReceiver(sender, receiver).isPresent()
                || friendRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent();

        if (alreadyRequested) {
            return false;
        }

        if (sender.getFriends().contains(receiver)) {
            return false;
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver, FriendRequest.Status.PENDING);
        friendRequestRepository.save(friendRequest);
        return true;
    }

    // Fetch incoming friend requests (where receiver is user and status = PENDING)
    public List<FriendRequest> getPendingRequestsForUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }
        return friendRequestRepository.findByReceiverAndStatus(userOpt.get(), FriendRequest.Status.PENDING);
    }

    // Fetch outgoing friend requests (where sender is user and status = PENDING)
    public List<FriendRequest> getPendingRequestsSentByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }
        return friendRequestRepository.findBySenderAndStatus(userOpt.get(), FriendRequest.Status.PENDING);
    }

    // Accept friend request: set status ACCEPTED and add friends both sides
    public void acceptFriendRequest(Long requestId) {
        Optional<FriendRequest> reqOpt = friendRequestRepository.findById(requestId);
        if (reqOpt.isEmpty()) return;

        FriendRequest request = reqOpt.get();
        request.setStatus(FriendRequest.Status.ACCEPTED);

        User sender = request.getSender();
        User receiver = request.getReceiver();

        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        friendRequestRepository.save(request);
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    // Decline friend request: set status DECLINED
    public void declineFriendRequest(Long requestId) {
        Optional<FriendRequest> reqOpt = friendRequestRepository.findById(requestId);
        if (reqOpt.isEmpty()) return;

        FriendRequest request = reqOpt.get();
        request.setStatus(FriendRequest.Status.DECLINED);
        friendRequestRepository.save(request);
    }
}
