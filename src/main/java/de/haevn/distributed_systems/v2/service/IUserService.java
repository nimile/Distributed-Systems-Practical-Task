package de.haevn.distributed_systems.v2.service;
import de.haevn.distributed_systems.v2.model.User;

import java.util.Optional;

public interface IUserService extends IService<User>{
    Optional<User> findByEmail(String mail);
}
