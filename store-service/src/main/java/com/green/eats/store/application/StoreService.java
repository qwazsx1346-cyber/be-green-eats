package com.green.eats.store.application;

import com.green.eats.common.model.ResultResponse;
import com.green.eats.store.application.model.MenuGetRes;
import com.green.eats.store.entity.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final MenuRepository menuRepository;

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
}
