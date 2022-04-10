package com.example.auctionappdemo.auction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

@Service
public class AuctionService {

    private AuctionRepository auctionRepository;

    private static final String[] ADJECTIVES = {"Niesamowity", "Jedyny taki", "IGŁA", "HIT", "Jak nowy", "Perełka", "OKAZJA", "Wyjątkowy"};

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
        updateCarTitles();
    }

    private void updateCarTitles() {
        List<Auction> all = auctionRepository.findAll();
        Random random = new Random();
        for (Auction auction : all) {
            String randomAdjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
            String title = String.format("%s %s %s", randomAdjective, auction.getCarMake(), auction.getCarModel());
            auction.setTitle(title);
        }
        auctionRepository.saveAll(all);
    }

    public List<Auction> find4MostExpensive() {
        return auctionRepository.findTop4ByOrderByPriceDesc();
    }

    Page<Auction> findAllForFiltersAndSort(AuctionFilters auctionFilters, Pageable pageable) {

        Specification<Auction> specification = Specification.where(null);

        specification = addSpecification(specification, auctionFilters.getTitle(), "title");
        specification = addSpecification(specification, auctionFilters.getCarModel(), "carModel");
        specification = addSpecification(specification, auctionFilters.getCarMaker(), "carMake");
        specification = addSpecification(specification, auctionFilters.getColor(), "color");

        return auctionRepository.findAll(specification, pageable);
    }

    private Specification<Auction> addSpecification(Specification<Auction> specification, String fieldValue, String fieldName) {
        if (!StringUtils.isEmpty(fieldValue)) {
            Specification<Auction> spec = specification.and((Specification<Auction>) (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(fieldName)), "%" + fieldValue.toUpperCase() + "%"));
            return specification.and(spec);
        }
        return specification;
    }
}
