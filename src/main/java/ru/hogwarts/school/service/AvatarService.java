package ru.hogwarts.school.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public List<Avatar> getAllPage(Integer numberPage, Integer sizePage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, sizePage);
        logger.info("Avatars page shown");
        return avatarRepository.findAll(pageRequest).getContent();
    }

}

