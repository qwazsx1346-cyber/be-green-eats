package com.green.eats.store.application;

import com.green.eats.common.model.MenuGetClientRes;
import com.green.eats.common.model.ResultResponse;
import com.green.eats.store.application.model.MenuGetRes;
import com.green.eats.store.application.model.MenuPostReq;
import com.green.eats.store.entity.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final MenuRepository menuRepository;

    public void addMenu(MenuPostReq req) {
        Menu menu = new Menu(req);
        menuRepository.save(menu);
    }

    public List<MenuGetRes> getAllMenus() {
        List<Menu> menuList = menuRepository.findAll(); //where절이 없어서 findAll잘 안씀.

        List<MenuGetRes> resList = new ArrayList<>( menuList.size() );
        //menuList.size() 을 넣어주면 한칸 더 늘리고 복사하고 하는 수고로움을 덜 수 있다.

        //?? 박스갈이 작업
        for (Menu menu : menuList) {
            MenuGetRes menuGetRes = new MenuGetRes( menu );
            resList.add( menuGetRes );

        }

        //보지마세요.
        List<MenuGetRes> resList2 = menuList.stream().map(MenuGetRes::new).toList();

        return resList;
    }

    public Map<Long, MenuGetClientRes> getMenuListByIds(List<Long> menuIds) {
        // 1. Repository에서 IN 절을 사용하여 일괄 조회
        List<Menu> menus = menuRepository.findAllById(menuIds);

        //여기부터 return map;까지 코드는 그 아래 Stream사용하는코드랑 똑같은거. 성능은 Stream보다 hashmap이 조금 더 나음
        Map<Long, MenuGetClientRes> map = new HashMap<>();

        for (Menu menu : menus) {
            Long key = menu.getId();
            MenuGetClientRes value = MenuGetClientRes.builder()
                                                    .menuId(menu.getId())
                                                    .name(menu.getName())
                                                    .price(menu.getPrice())
                                                    .build();
            map.put(key, value);
        }
        return map;

        // 2. List > Map 변환 (Java Stream 사용)
//        return menus.stream()
//            .collect(Collectors.toMap( Menu::getId, menu -> MenuGetClientRes.builder()
//                .menuId(menu.getId())
//                .name(menu.getName())
//                .price(menu.getPrice())
//                .build()
//            ));
    }
}
