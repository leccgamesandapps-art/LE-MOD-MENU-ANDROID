cmake_minimum_required(VERSION 3.22.1)
project("lemodmenu")

add_library(lemodmenu SHARED
    Main.cpp
    Menu/Menu.cpp
    Includes/Utils.cpp
)

find_library(log-lib log)
find_library(android-lib android)

target_link_libraries(lemodmenu
    ${log-lib}
    ${android-lib}
)

target_include_directories(lemodmenu PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/Includes
    ${CMAKE_CURRENT_SOURCE_DIR}/Menu
)
