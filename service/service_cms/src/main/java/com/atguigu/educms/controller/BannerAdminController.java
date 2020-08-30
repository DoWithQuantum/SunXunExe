package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-16
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {
    @Autowired
    private CrmBannerService crmBannerService;

    //分页查询 crmBannerPage是分页后所有的查询结果  crmBannerService.page(crmBannerPage, null)返回的是page页的内容
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page, @PathVariable long limit) {
        Page<CrmBanner> crmBannerPage = new Page<>(page, limit);
        crmBannerService.page(crmBannerPage, null);
        return R.ok().data("items", crmBannerPage.getRecords()).data("total", crmBannerPage.getTotal());
    }

    //添加banner
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner) {
        crmBannerService.save(crmBanner);
        return R.ok();
    }

    //删除banner
    @DeleteMapping("removeBanner/{id}")
    public R removeBanner(@PathVariable String id) {
        crmBannerService.removeById(id);
        return R.ok();
    }

    //更改bnner
    @PostMapping("updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner) {
        crmBannerService.updateById(crmBanner);
        return R.ok();
    }

}

