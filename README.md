# Stream
Stream 사용하기 [인프런-백기선-더 자바, Java 8](https://www.inflearn.com/course/the-java-java8)

## Stream 이란? ##
1. sequence of elements supporting sequential and parallel aggregate operations
2. 데이터를 담고 있는 저장소 (컬렉션)이 아니다.
3. Funtional in nature, 스트림이 처리하는 데이터 소스를 변경하지 않는다.
4. 스트림으로 처리하는 데이터는 오직 한번만 처리한다.
5. 무제한일 수도 있다. (Short Circuit 메소드를 사용해서 제한할 수 있다.)
6. 중개 오퍼레이션은 근본적으로 lazy 하다.
7. 손쉽게 병렬 처리할 수 있다.
````java
// 병렬처리가 무조건 속도가 빠른게 아니다.  // 데이터가 방대하게 큰 경우에 사용, 아니면 stream 사용
List<String> collect = names.parallelStream().map((s) -> {
	System.out.println(s);
	return s.toUpperCase();
}).collect(Collectors.toList());
````

## 스트림 파이프라인 ##
- 0 또는 다수의 중개 오퍼레이션 (intermediate operation)과 한개의 종료 오퍼레이션 (terminal operation)으로 구성한다.
- 스트림의 데이터 소스는 오직 터미널 오퍼네이션을 실행할 때에만 처리한다.

## 중개 오퍼레이션 (중간연산) ##
- Stream을 리턴한다.
- Stateless / Stateful 오퍼레이션으로 더 상세하게 구분할 수도 있다.    
(대부분은 Stateless지만 distinct나 sorted 처럼 이전 이전 소스 데이터를 참조해야 하는 오퍼레이션은 Stateful 오퍼레이션이다.)
- filter, map, limit, skip, sorted, peek

````java
List<String> names = new ArrayList<>();
names.add("a");
names.add("b");
names.add("c");


names.stream().map((s) -> {
	System.out.println(s); // <- 중개형 오퍼레이션이기 때문에 실행되지 않음
	return s.toUpperCase(); 
});

// 결론: 중개 오퍼레이션은 근본적으로 lazy 하다. ( 물론 다른뜻도 있긴함 )

System.out.println("test");
````

## 종료 오퍼레이션 (최종연산) ##
- Stream을 리턴하지 않는다.
- collect, allMatch, count, forEach, min, max, ...

````java
List<String> names = new ArrayList<>();
names.add("a");
names.add("b");
names.add("c");


names.stream().map((s) -> {
	System.out.println(s);  // 종료형 오퍼레이션 이기때문에 실행됨
	return s.toUpperCase();
}).collect(Collectors.toList()); // <- 종료형 오퍼레이션

names.forEach(System.out::println);
````

## Stream API ##

### 1. Filter ###
- 중개 오퍼레이션 (중간연산)
- Stream<T> filter(Predicate<? super T> predicate)

````java
// 예제
List<Integer> names = new ArrayList<>();
names.add(1);
names.add(2);
names.add(3);
names.add(4);

names.stream()
	 .filter(n -> n > 2)
	 .forEach(System.out::println);
````

````java
// 혹은 특정 객체의 인스턴스 참조를 통한 filter
names.stream()
	.filter(NumberUtil::isGreaterThanTwo) // 메소드 레퍼런스
	.forEach(System.out::println);

public class NumberUtil {
	static boolean isGreaterThanTwo(int num) {
		return num > 2;
	}
}
````

[메소드레퍼런스참조](https://github.com/orange601/Java-Lambda/blob/main/README.md#6-%EB%A9%94%EC%86%8C%EB%93%9C-%EB%A0%88%ED%8D%BC%EB%9F%B0%EC%8A%A4)

### 2. Map ###
- 중개 오퍼레이션
- 새로운 Stream을 형성하는 연산
- 저장된 값을 특정한 형태로 변환하는데 주로 사용, 데이터 타입도 변경

````java
        List<String> alpha = Arrays.asList("a", "b", "c", "d");

        //Before Java8
        List<String> alphaUpper = new ArrayList<>();
        for (String s : alpha) {
            alphaUpper.add(s.toUpperCase());
        }

        System.out.println(alpha); //[a, b, c, d]
        System.out.println(alphaUpper); //[A, B, C, D]

        // Java 8
        List<String> collect = alpha.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect); //[A, B, C, D]
````

### 3. generate ###
- 중개 오퍼레이션
- static <T> Stream<T> generate(Supplier<T> s)
- 람다식을 매개변수로 받아서, 이 람다식에 의해 계산되는 값들을 요소로 하는 무한 스트림을 생성한다.
- generate()에 의해 생성된 스트림은 기본형 스트림 타입의 참조변수로 다룰 수 없다.
- iterate()와 비슷하나 이전 결과를 이용해 다음 요소를 계산하지 않음.

````java
Stream<String> stream = Stream.generate(() -> "Echo").limit(5); // limit 설정이 없으면 무한으로 생성됨
stream.forEach(System.out::println);
````

### 4. limit ###
- 중개 오퍼레이션 (중간연산)

````java
List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10" );
Stream<String> stream1 = list.stream();
Stream<String> stream2 = stream1.limit(5);
stream2.forEach(System.out::println);
````

### 5. skip(long)
- 중개 오퍼레이션 (중간연산)
- skip()은 limit()의 반대
- Stream.skip(number)에서 인자로 전달된 숫자만큼 요소를 건너띄고, 그 이후의 요소들로만 스트림을 생성. Stream의 초기 데이터를 무시할 때 사용할 수 있다.

````java
List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10" );
Stream<String> stream3 = list.stream();
Stream<String> stream4 = stream3.skip(5);
stream4.forEach(System.out::println);
````
````
// output
6
7
8
9
10
````

6. 스트림에 있는 데이터가 특정 조건을 만족하는지 확인
	- anyMatch(), allMatch(), nonMatch()
	- 예) k로 시작하는 문자열이 있는지 확인한다. (true 또는 false를 리턴한다.)
	- 예) 스트림에 있는 모든 값이 10보다 작은지 확인한다.


### 7. count ###
- 종료 오퍼레이션(최종연산)

````java
List<String> words = Arrays.asList("aop", "ioc", "okay", "true", "false");
int count = (int) words.stream().filter(w->w.contains("o")).count();
````

8. 스트림을 데이터 하나로 뭉치기
	- reduce(identity, BiFunction), collect(), sum(), max()
	- 예) 모든 숫자 합 구하기
	- 예) 모든 데이터를 하나의 List 또는 Set에 옮겨 담기
	
### 9. Peek ###
- 스트림에는 중간 연산의 수행 결과를 디버깅할 수 있는 수단인 peek 메서드를 제공한다.
- peek은 stream 을 return 하기때문에 최종 연산으로 사용 불가능하다.
- peek메서드와 forEach 메서드를 혼동해서는 안된다. peek은 중간연산, forEach는 최종연산
	
:-1: BAD
	
````java
// peek() 메소드를 최종 연산으로 사용하면 동작하지 않는다.
public static void main(String[] args) {
	Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	stream.filter(s -> s % 3 == 0)
	      .peek(s -> System.out.println("원본 스트림 : " + s));
}
````

### 10. FlatMap  ###
- Map의 경우 이중 for문 해결을 못하는 이슈가 있다. 
- flatmap은 첫번째 받은 데이터를 다시 stream으로 리턴해서 for를 한번만 수행해도 같은 결과를 얻을 수 있다.

````java
String[][] data = new String[][]{ {"1", "2"}, {"3", "4"} };

Stream<Stream<String>> map = Arrays.stream(data).map(x -> Arrays.stream(x));
map.forEach(s -> s.forEach(System.out::println));

Stream<String> flatmap = Arrays.stream(data).flatMap(x -> Arrays.stream(x));
flatmap.forEach(System.out::println);
````

### 11. Max ###
- 종료 오퍼레이션 (최종연산)

````java
List<Integer> intList = Arrays.asList(2, 3, 6, 4, 10, 23);
Integer maxValue = intList.stream()
	.mapToInt(x -> x)
        .max()
        .orElseThrow(NoSuchElementException::new);
````

### 12. Min ###
- 종료 오퍼레이션 (최종연산)

````java
List<Integer> numbers = List.of(4, 0, 5, 2, 7, 1, 8, 6, 9, 3);
int min = numbers.isEmpty() ? -1 : Collections.min(numbers);
System.out.println("Min: " + min);
````

````java
List<Integer> numbers = List.of(4, 0, 5, 2, 7, 1, 8, 6, 9, 3);
int max = numbers.stream().max(Integer::compare).orElse(-1);
System.out.println("Max: " + max); // Max: 9
````

### 13. Collect ###
- 종료 오퍼레이션 (최종연산)

````
// given
createMember(3, createTeam("Korea"));
createMember(3, createTeam("Japan"));

// when
Map<String, List<MemberEntity>> actual = memberRepository.findAllWithCountry()
        .stream()
        .collect(Collectors.groupingBy(
                MemberByCountryProjection::getCountry,
                HashMap::new,
                Collectors.mapping(MemberByCountryProjection::getMember, Collectors.toList())));

// then
assertEquals(actual.get("Korea").size(), 3);
assertEquals(actual.get("Japan").size(), 3);
````

## 예제 ##
````java
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
````

## Spliterator ##
- Iterator 처럼 Spliterator는 소스의 요소 탐색 기능을 제공 But, Spliterator는 병렬 작업에 특화되어 있다.
- Stream에서 사용된다.

### 메서드 ###
- tryAdvance : 요소를 하나씩 소비하면서 탐색해야 할 요소가 남아있으면 true 반환
- trySplit : 일부 요소를 분할해서 두 번째 Spliterator를 생성
- estimateSize : 탐색해야 할 요소의 수 제공
- characteristics : Spliterator 객체에 포함된 모든 특성값의 합을 반환
- 각 특성은 어떤 Spliterator 객체인가에 따라 다르며 그에 따른 각 메서드들의 내부적인 동작이 다를 수 있다.
	
````java
List<String> list = new ArrayList<>();
list.add("test1");
list.add("test2");
list.add("test3");
list.add("test4");
list.add("test5");
list.add("test6");		

Spliterator<String> alist = list.spliterator();
alist.forEachRemaining((p) -> {
	System.out.println(p);
});

System.out.println("======================");

Spliterator<String> blist = list.spliterator();
Spliterator<String> spliterators2 = blist.trySplit();
while(spliterators2.tryAdvance(System.out::println));

// 결과
// test1
// test2
// test3
// test4
// test5
// test6
// ======================
// test1
// test2
// test3
````

## Arrays.stream VS Stream.of ##

### Arrays.stream ###
````java
// 래퍼런스타입
String[] strArray = {"A", "B", "C"};
Stream<String> strStream = Arrays.stream(strArray);


// 기본 타입_1
Integer[] intArray = {1, 2, 3, 4, 5};
Stream<Integer> intStream = Arrays.stream(intArray);


// 기본 타입_2
// Java 1.8+ 이상인 경우 기본 타입의 배열을 스트림으로 변환할 수 있다.
Integer[] intArray = {1, 2, 3, 4, 5};
IntStream intStream = Arrays.stream(intArray);
````
	
### Stream.of ###
````java
// 참조타입
String[] strArray = {"A", "B", "C"};
Stream<String> strStream = Stream.of(strArray);

// 기본타입_1
int[] intArray = {1, 2, 3, 4, 5};
Stream<int[]> intStream = Stream.of(intArray);
intStream.forEach(System.out::print); // 결과: [I@2f410acf  // 주소값

// 기본타입_2
int[] intArray = {1, 2, 3, 4, 5};
IntStream intStream = IntStream.of(intArray); // IntStream을 이용
intStream.forEach(System.out::print); // 결과: 12345
````

## Enum filter ##
````java
Arrays.stream(EnumClass.values())
	.filter(c -> c.getSymbol().equals(name))
	.findAny()
	.orElseThrow(() -> new Exception());
````
