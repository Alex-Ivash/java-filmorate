package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.entity.mpa.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;

    @Override
    public Mpa create(Mpa entity) {
        return mpaRepository.create(entity);
    }

    @Override
    public Mpa find(long id) {
        return checkMpaExistence(id);
    }

    @Override
    public Mpa update(Mpa newEntity) {
        checkMpaExistence(newEntity.getId());
        return mpaRepository.update(newEntity);
    }

    @Override
    public List<Mpa> getAll() {
        return mpaRepository.findAll();
    }

    @Override
    public void remove(long id) {
        mpaRepository.remove(id);
    }

    @Override
    public void removeAll() {
        mpaRepository.removeAll();
    }

    private Mpa checkMpaExistence(long id) {
        return mpaRepository.find(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг Mpa с id=" + id + " не найден"));
    }
}
