package com.example.movie.wish.list;

import org.springframework.boot.SpringApplication;

public class TestMovieWishListApplication {

	public static void main(String[] args) {
		SpringApplication.from(MovieWishListApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
