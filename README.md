# Stream
Stream 소개 (with. java 8)

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

## 중개 오퍼레이션 ##
- Stream을 리턴한다.
- Stateless / Stateful 오퍼레이션으로 더 상세하게 구분할 수도 있다.    
(대부분은 Stateless지만 distinct나 sorted 처럼 이전 이전 소스 데이터를 참조해야 하는 오퍼레이션은 Stateful 오퍼레이션이다.)
- filter, map, limit, skip, sorted, ...

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

## 종료 오퍼레이션 ##
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
1. 걸러내기
	- Filter(Predicate)
	- 예) 이름이 3글자 이상인 데이터만 새로운 스트림으로 

2. 변경하기
	- Map(Function) 또는 FlatMap(Function)
	- 예) 각각의 Post 인스턴스에서 String title만 새로운 스트림으로
	- 예) List<Stream<String>>을 String의 스트림으로

3. 생성하기
	- generate(Supplier) 또는 Iterate(T seed, UnaryOperator) 
	- 예) 10부터 1씩 증가하는 무제한 숫자 스트림
	- 예) 랜덤 int 무제한 스트림

4. 제한하기
	- limit(long) 또는 skip(long)
	- 예) 최대 5개의 요소가 담긴 스트림을 리턴한다.
	- 예) 앞에서 3개를 뺀 나머지 스트림을 리턴한다.

5. 스트림에 있는 데이터가 특정 조건을 만족하는지 확인
	- anyMatch(), allMatch(), nonMatch()
	- 예) k로 시작하는 문자열이 있는지 확인한다. (true 또는 false를 리턴한다.)
	- 예) 스트림에 있는 모든 값이 10보다 작은지 확인한다.

6. 개수 세기
	- count()
	- 예) 10보다 큰 수의 개수를 센다.

7. 스트림을 데이터 하나로 뭉치기
	- reduce(identity, BiFunction), collect(), sum(), max()
	- 예) 모든 숫자 합 구하기
	- 예) 모든 데이터를 하나의 List 또는 Set에 옮겨 담기

