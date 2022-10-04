package com.date.time;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {
	public static void main(String[] args) {
		List<OnlineClass> springClasses = new ArrayList<>();
		springClasses.add(new OnlineClass(1, "spring boot", true));
		springClasses.add(new OnlineClass(2, "spring data jpa", true));
		springClasses.add(new OnlineClass(3, "spring mvc", false));
		springClasses.add(new OnlineClass(4, "spring core", false));
		springClasses.add(new OnlineClass(5, "rest api development", false));
		
		
		// spring으로 시작하는 수업
		springClasses.stream()
				.filter(sc -> sc.getTitle().startsWith("spring"))
				.forEach(sc -> System.out.println(sc.getId()));
		
		// close 되지 않은 수업
		springClasses.stream()
			.filter(Predicate.not(OnlineClass::isClosed)) //.filter(sc -> !sc.isClosed()) 이렇게 한것과 같은 의미
			.forEach(sc -> System.out.println(sc.getId()));
		
		// 수업 이름만 모아서1
		springClasses.stream()
			.map(sc -> sc.getTitle()) // map은 타입을 변경할 수 있음
			.forEach(s -> System.out.println(s)); // 타입이 String으로 변경됐기때문에 인자가 변경됐다.
		
		// 수업 이름만 모아서2
		springClasses.stream()
			.map(OnlineClass::getTitle)
			.forEach(System.out::println);
		
		
		///////////////////////////////////////////////////////////////////
		
		/** 리스트 안에 리스트 */
		List<OnlineClass> javaClasses = new ArrayList<>();
		javaClasses.add(new OnlineClass(6, "The Java, Test", true));
		javaClasses.add(new OnlineClass(7, "The Java, Code manipulation", true));
		javaClasses.add(new OnlineClass(8, "The Java, 8 to 11", false));
		
		List<List<OnlineClass>> orangeEvents = new ArrayList<>();
		orangeEvents.add(springClasses);
		orangeEvents.add(javaClasses);
		
		// flatMap: 모든 리스트의 원소를 다룸
		
		System.out.println("두 수업 목록에 들어있는 모든 수업 아이디 출력");
		orangeEvents.stream().flatMap(Collection::stream) // flatMap(list -> list.stream())  // list 타입을 stream 타입으로 변경한다.
			.forEach(oc -> System.out.println(oc.getId())); // stream으로 변경된 list의 원소 출력
		
		
		System.out.println("10부터 1씩 증가하는 무제한 스트림 중에서 앞에 10개 빼고 최대 10개까지만");
		Stream.iterate(10,  i -> i + 1) // 무제한 스트림 // 중계형 오퍼레이터
			.skip(10)
			.limit(10)
			.forEach(System.out::println);
		
		System.out.println("자바 수정 중에 Test가 들어있는 수업이 있는지 확인");
		boolean isExist = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test"));
		System.out.println(isExist);
		
		System.out.println("스프링 수업 중에 제목에 spring이 들어간 제목만 모아서 List로 만들기11");
		springClasses.stream()
			.filter(oc -> oc.getTitle().contains("spring"))
			.map(oc -> oc.getTitle())
			.forEach(System.out::println);
		
		System.out.println("스프링 수업 중에 제목에 spring이 들어간 제목만 모아서 List로 만들기22");
		springClasses.stream()
			.map(oc -> oc.getTitle()) // map이 먼저 올 수 있지만
			.filter(t -> t.contains("spring")) // 타입이 변경된다는것을 알아야한다.
		.forEach(System.out::println);
	}
	
}
