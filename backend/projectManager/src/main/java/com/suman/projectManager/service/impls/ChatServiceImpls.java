package com.suman.projectManager.service.impls;

import com.suman.projectManager.entity.ChatEntity;
import com.suman.projectManager.repository.ChatRepository;
import com.suman.projectManager.service.interfac.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpls implements ChatService {

    private final ChatRepository chatRepository;
    @Override
    public ChatEntity createChat(ChatEntity chat) {
       return chatRepository.save(chat);
    }
}
