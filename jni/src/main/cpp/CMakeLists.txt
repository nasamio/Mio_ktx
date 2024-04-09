cmake_minimum_required(VERSION 3.22.1) # 这个和build中的cmake的版本保持一致

#project(jni)                # 这个好像没有也行

add_library(
        mio_jni             # so库名称
        SHARED              # 动态
        NativeHelper.cpp    # 具体实现类
)
add_library(
        socaffinity
        SHARED
        Socaffinity.cpp
)


find_library(
        log-lib             # 链接已有库，这里相当于别名
        log                 # 已有库名
)

target_link_libraries(      # 把log库链接到我们的库上
        mio_jni
        ${log-lib}
)

target_link_libraries(
        socaffinity
        android
        log
)