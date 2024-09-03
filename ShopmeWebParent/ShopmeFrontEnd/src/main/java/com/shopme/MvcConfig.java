package com.shopme;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("user-photos", registry);
        exposeDirectory("category-images", registry);
        exposeDirectory("brand-logos", registry);
        exposeDirectory("product-images", registry);
        exposeDirectory("site-logo", registry);
    }

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:/" + absolutePath + "/");
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String dirName = "user-photos";
//        Path userPhotosDir = Paths.get(dirName);
//        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
//
//        registry.addResourceHandler("/" + dirName + "/**")
//                .addResourceLocations("file:/" + userPhotosPath + "/");
//
//        String categoryImagesName = "category-images";
//        Path categoryImagesPhotosDir = Paths.get(categoryImagesName);
//        String categoryImagesPhotosPath = categoryImagesPhotosDir.toFile().getAbsolutePath();
//
//        registry.addResourceHandler("/" + categoryImagesName + "/**")
//                .addResourceLocations("file:/" + categoryImagesPhotosPath + "/");
//
//        String brandLogoDirName = "brand-logos";
//        Path brandLogoDir = Paths.get(brandLogoDirName);
//        String brandLogosPath = brandLogoDir.toFile().getAbsolutePath();
//
//        registry.addResourceHandler("/" + brandLogoDirName + "/**")
//                .addResourceLocations("file:/" + brandLogosPath + "/");
//
//        String productLogoDirName = "product-images";
//        Path productLogoDir = Paths.get(productLogoDirName);
//        String productLogosPath = productLogoDir.toFile().getAbsolutePath();
//
//        registry.addResourceHandler("/" + productLogoDirName + "/**")
//                .addResourceLocations("file:/" + productLogosPath + "/");
//    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new PagingAndSortingArgumentResolver());
//    }

}