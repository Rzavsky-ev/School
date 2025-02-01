package ru.hogwarts.school.service;


import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;


import java.util.List;


@Service
@Transactional
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public List<Avatar> getAllPage(Integer numberPage, Integer sizePage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, sizePage);
        return avatarRepository.findAll(pageRequest).getContent();
    }

}

