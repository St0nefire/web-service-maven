package sunwell.stonefire.base.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sunwell.stonefire.base.repo.MasterMenuRepo;
import sunwell.stonefire.base.repo.ScheduledPackageRepo;
import sunwell.stonefire.core.entity.MasterMenu;
import sunwell.stonefire.core.entity.ScheduledPackage;

@Service
@Transactional
public class MasterMenuSvc {
	
	@Autowired
	MasterMenuRepo mmRepo;
	
	public Page<MasterMenu> findAllMasterMenu(Pageable _page) {
		return mmRepo.findAll(_page);
	}
	
	public List<MasterMenu> findAllMasterMenu() {
		return mmRepo.findAll();
	}
}
