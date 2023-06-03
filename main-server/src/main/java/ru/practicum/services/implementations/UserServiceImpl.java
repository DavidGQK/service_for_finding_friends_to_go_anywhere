package ru.practicum.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.errors.exceptions.NotFoundException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.User;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        if ((userDto.getId() != null) && (userDto.getId() != 0)) {
            throw new IllegalArgumentException("The original user id must be 0");
        }
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The user with this id does not exist"));
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> find(Long[] id, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (id == null) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findByIds(id, pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }
}