package pl.pacinho.MasterBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.pacinho.MasterBet.entities.Match;
import pl.pacinho.MasterBet.repositories.MatchRepository;
import pl.pacinho.MasterBet.utils.pagination.Paged;
import pl.pacinho.MasterBet.utils.pagination.Paging;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    public Page<Match> findPaginated(Pageable pageable) {
        List<Match> matches = matchRepository.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Match> list;

        if (matches.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, matches.size());
            list = matches.subList(startItem, toIndex);
        }

        Page<Match> matchPage = new PageImpl<Match>(list, PageRequest.of(currentPage, pageSize), matches.size());

        return matchPage;
    }

    public Paged<Match> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size);
        Page<Match> postPage = matchRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public Paged<Match> getPagesWhereDateIsAfterNow(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("eventDate").ascending());
        Page<Match> postPage = matchRepository.findAllByEventDateGreaterThan(new Date(), request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public Paged<Match> getPagesWhereDateIsBeforeNow(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("eventDate").descending());
        Page<Match> postPage = matchRepository.findAllByEventDateLessThan(new Date(), request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public void updateScore(Match match) {
        Optional<Match> byId = matchRepository.findById(match.getId());
        if (byId.isPresent()) {
            Match match1 = byId.get();
            match1.setScoreA(match.getScoreA());
            match1.setScoreB(match.getScoreB());
            matchRepository.save(match1);
        }
    }

    public Paged<Match> getPagesWhereDateIsBeforeNowAndScoreIsNotNull(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("eventDate").descending());
        Page<Match> postPage = matchRepository.findAllByScoreAIsNotNullAndEventDateLessThan(new Date(), request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public Paged<Match> getPagesWhereDateIsBeforeNowAndScoreIsNull(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("eventDate").descending());
        Page<Match> postPage = matchRepository.findAllByScoreAIsNullAndEventDateLessThan(new Date(), request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public List<Match> getLast10MatchesWithScore() {
        return matchRepository.findFirst10ByScoreAIsNotNullAndEventDateLessThanOrderByEventDateDesc(new Date());
    }

    public Match getById(long id) {
        return matchRepository.findById(id).get();
    }
}
