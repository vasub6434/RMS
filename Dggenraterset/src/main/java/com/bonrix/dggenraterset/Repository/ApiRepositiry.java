package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.Apikey;

public interface ApiRepositiry extends BaseRepository<Apikey, Long> {

	Apikey findByuid(Long uid);

	Apikey findBykeyValue(String key);


}
