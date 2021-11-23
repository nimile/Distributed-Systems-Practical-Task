package de.haevn.distributed_systems.v2.utils.sequence_generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(SequenceGeneratorService.class);

    @Autowired
    private MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        logger.info("Create new id for {}", seqName);
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public void resetSequence(String seqName){
        logger.info("Reset all ids for {}", seqName);
        mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().set("seq", 0), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
    }
}
