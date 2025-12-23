### SPRINT 2 MISSION
1. `JCF*Service` 구현체와 `File*Service` 구현체를 비교하여 공통점과 차이점을 발견해보세요.
    - JCF*Service와 File*Service의 구현체의 전체적인 비즈니스 기능 구조는 동일하다.
        - 객체 생성, 수정, 삭제, 단일 조회, 전체 조회 기능
        - 메시지 생성 시 검증 로직, 채널에 메시지 등록, 채널에 유저 입장,퇴장 등
    - 하지만 `JCF*Service`와 `File*Service` 구현체의 가장 큰 차이점은 저장 방식이다.
        - JCF의 경우 데이터가 메모리에 저장되는 것이기 때문에 프로그램이 종료되면 데이터도 사라진다.
        - `Map<UUID, User>` / `Map<UUID, Channel>` / `Map<UUID, Message>` 로 데이터를 관리
        - File의 경우 객체를 직렬화하여 파일에 저장(`save()`)하고 필요할 때는 파일을 불러와 역직렬화하여 객체에 저장한다.(`load()`)
        - 파일로 저장하기 때문에 프로그램이 종료되어도 파일은 남아있다.
        - 다시 프로그램을 실행하면 지난 데이터 이후로 작업할 수 있다.

2. 이전에 작성했던 코드(`JCF*Service` 또는 `File*Service`)와 비교해 어떤 차이가 있는지 정리해보세요.
    - 기존에 작성했던 코드는 비즈니스 로직과 저장 로직이 한 곳에 모아있었다.
    - 하지만 repository를 필요로하는 `Basic*Servic`e를 구현하면서
    - 비즈니스 로직과 저장 로직을 분리했다. (관심사의 분리)
    - SOLID 원칙의 단일 책임 원칙에서 볼 때 기존에는 데이터 관리 + 출력 관리로 책임이 혼합되어 있었지만
    - `Basic*Service`는 비즈니스 로직만 담당하고 데이터 관리는 repository에게 책임을 맡겼다.

### SPRINT 3 MISSION
1. `JavaApplication`과 `DiscodeitApplication`에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.
IoC Container
- `JavaApplication` 같은 경우 `new` 연산자를 통해서 내가 직접 객체를 생성하고 초기화 하는 방식
-  하지만, `DiscodeitApplication` 경우 `new` 연산자를 사용하지 않아도 IoC Container가 객체 생성을 대신 해줘서 개발자가 할 일을 덜어준다.
Dependency Injection
- 의존성 주입이 필요한 경우에도 생성자 주입을 통해서 직접 의존 관계를 주입했었다.
- 하지만, `DiscodeitApplication` 경우 `new` 연산자를 사용하지 않아도 IoC Container가 @Autowired 어노테이션을 통해 의존성 주입 또한 책임진다.
Bean
- `JavaApplication` 경우에는 객체를 생성하고 소멸하고 관리는 전부 관리자의 책임이었다. 스프린트 미션 2 때 구현했던 싱글턴 패턴 또한 개발자(나)가 직접 하드코딩 했었다.
- `DiscodeitApplication`의 경우 IoC Container가 관리하는 Bean들의 생명주기, 초기화와 소멸, 싱글턴 보장 등 알아서 관리해준다.