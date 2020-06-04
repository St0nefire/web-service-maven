package sunwell.stonefire.base.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sunwell.stonefire.base.repo.ScheduledPackageRepo;
import sunwell.stonefire.core.entity.ScheduledPackage;

@Service
@Transactional
public class ScheduledPackageSvc {
	
	@Autowired
	ScheduledPackageRepo spRepo;
	
	public Page<ScheduledPackage> findAllScheduledPackage(Pageable _page) {
		return spRepo.findAll(_page);
	}
}
