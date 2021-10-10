package nia.corewebapp.twitter.service;

import nia.corewebapp.twitter.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionOperations;
import nia.corewebapp.twitter.repository.TagRepository;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final TransactionOperations transactionOperations;

    @Autowired
    public TagService(TagRepository tagRepository,
                      TransactionOperations transactionOperations) {
        this.tagRepository = tagRepository;
        this.transactionOperations = transactionOperations;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTag(String name){
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
    }


}
