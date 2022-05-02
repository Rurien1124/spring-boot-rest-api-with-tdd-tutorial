> # 스프링 부트 REST API TDD 연습 프로젝트

- ## 목차
- [1. REST API란 무엇인가?](#1-rest-api란-무엇인가)
  - [1-1. REST API의 조건](#1-1-rest-api의-조건)
  - [1-2. Uniform interface란?](#1-2-uniform-interface란)
    * [리소스 식별(identification of resources)](#리소스-식별identification-of-resources)
    * [표현을 통한 리소스 처리(manipulation of resources through representations)](#표현을-통한-리소스-처리manipulation-of-resources-through-representations)
    * [자기 서술형 메시지(self-descriptive messages)](#자기-서술형-메시지self-descriptive-messages)
    * [애플리케이션 상태 엔진으로서의 하이퍼미디어(**H**ypermedia **A**s **T**he **E**ngine **O**f **A**pplication **S**tate)](#애플리케이션-상태-엔진으로서의-하이퍼미디어hypermedia-as-the-engine-of-application-state)
- [2. EventControllerTest](#2-eventcontrollertest)
  - [2-1. Class annotations](#2-1-class-annotations)
  - [2-2. MockMvc](#2-2-mockmvc)
- [3. EventController](#3-eventcontroller)
  - [3-1. Validation](#3-1-validation)
  - [3-2. ResponseEntity](#3-2-responseentity)
  - [3-3. Parameters](#3-3-parameters)
- [4. Spring HATEOAS 기능](#4-spring-hateoas-기능)
  - [4-1. 링크 작성 기능](#4-1-링크-작성-기능)
  - [4-2. 리소스 생성 기능](#4-2-리소스-생성-기능)
  - [4-3. Relation(REL)](#4-3-relationrel)
  - [4-4. Deprecated classes](#4-4-deprecated-classes)

> <small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>

- ## 1. REST API란 무엇인가?
  - **RE**presentational **S**tate **T**ransfer의 약자
  - 시스템 각각(ex - frontend/backend)의 독립적인 진화를 보장

  - ## 1-1. REST API의 조건
    - client-server         : 서버와 클라이언트의 구조를 분리하여 의존성 감소
    - stateless             : 세션/쿠키 등의 정보를 가지고 있지 않음(ex - 토큰)
    - cache                 : HTTP의 캐싱 기능을 사용할 수 있음
    - **uniform interface** : 리소스 식별, 표현을 통한 리소스 처리, 자기 서술형 메시지, 애플리케이션 상태 엔진으로서의 하이퍼미디어
    - layered system        : 다중 계층(보안, 로드 밸런싱, 암호화 등)으로 구성하여 PROXY, 게이트웨이 등 네트워크 기반의 중간 매체를 사용할 수 있게 함
    - code-on-demand        : 클라이언트가 처리할 코드를 서버가 제공하는 것(ex - javascript)
  
  - ## 1-2. Uniform interface란?
    > ### 리소스 식별(identification of resources)
    - 각각의 리소스는 URI를 구분자로 식별 가능해야 한다
    ```
    http://www.domain.com/user   // User의 collection
    http://www.domain.com/user/1 // id의 값이 1인 user
    http://www.domain.com/user/4 // id의 값이 4인 user
    ```
    
    > ### 표현을 통한 리소스 처리(manipulation of resources through representations)
    - 리소스 자체가 아닌 리소스의 표현(GET, POST, PUT, PATCH, DELETE ... 즉, 하고자 하는 행동)을 전송
    ```
    GET /user    // 전체 user를 조회
    POST /user/1 // id의 값이 1인 user를 생성
    PUT /user/4  // id의 값이 4인 user를 수정
    ```
    
    > ### 자기 서술형 메시지(self-descriptive messages)
    - 자신을 어떻게 처리해야 할 지에 대한 정보를 가지고 있어야 함. 메시지의 내용으로 해석이 가능하여야 함
    ```
    // 1-1. 목적지가 빠진 요청 메시지
    GET / HTTP/1.1
    
    // 1-2. 목적지가 추가된 요청 메시지
    GET / HTTP/1.1
    Host: www.domain.com
    
    
    // 2-1. 컨텐츠 타입이 빠진 응답 메시지
    HTTP/1.1 200 OK
    {"id": 1}
    
    // 2-2. 컨텐츠/미디어 타입이 추가된 응답 메시지
    HTTP/1.1 200 OK
    Content-Type: application/json-patch+json
    {"id": 1}
    ```
    
    > ### 애플리케이션 상태 엔진으로서의 하이퍼미디어(**H**ypermedia **A**s **T**he **E**ngine **O**f **A**pplication **S**tate)
    - 애플리케이션의 상태는 Hyperlink를 통해 전이되어야 함
    - 이를 통해 서버의 URI가 변경되는 경우가 발생하여도 클라이언트에 영향이 가지 않음
    ```
    // 1. HATEOAS가 빠진 응답 메시지
    HTTP/1.1 200 OK
    Content-Type: application/json-patch+json
    {"id": 2}
    
    // 2. HATEOAS가 추가된 응답 메시지
    HTTP/1.1 200 OK
    Content-Type: application/json-patch+json
    Link: </user/1>; rel="previous", </user/3>; rel="previous";
    {"id": 2}
    ```
  
- ## 2. EventControllerTest
  - ## 2-1. Class annotations
    - Controller 테스트의 경우 mocking 해야할 범위가 넓어지기 때문에, @SpringBootTest를 사용
  
  - ## 2-2. MockMvc
    - Controller의 API를 테스트하기 위해 의존성을 주입하여 테스트
    - andExpect(ResultMatcher)를 사용하여 header, json을 검증
    
- ## 3. EventController
  - ## 3-1. Validation
    - @Valid 어노테이션을 통한 validation
    - @Valid 어노테이션에서 validation 할 수 없는 경우에는 별도의 클래스를 생성하여 validation
    - 입력 시 불필요한 파라미터를 제한하기 위해 Entity를 Request에 직접 사용하지 않고, 별도로 DTO 클래스를 만들어 사용
    
  - ## 3-2. ResponseEntity
    - 응답 HttpStatus를 지정할 수 있음(.badRequest(), .ok() 등)
    - 응답 Body를 지정할 수 있음(JSON 형태 응답의 경우 serialization을 해주어야 함)
    
  - ## 3-3. Parameters
    > ### @PathVariable
    - GET /user/1
    ```
    // User의 ID로 조회(PK)
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariavle("id") int id){}
    ```
    
    > ### @QueryParam / @RequestParam
    - GET /user?name=gchyoo
    - 둘의 차이는? QueryParam은 값이 들어오지 않아도 되지만, RequestParam은 값이 들어오지 않으면 오류 발생
    ```
    // User의 name으로 조회(Not PK)
    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> findUserByQueryParam(@QueryParam("name") String name) {}
    
    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> findUserByRequestParam(@RequestParam("name") String name) {}
    ```
    
    > ### @RequestBody
    - POST /user
      Body {
        "id": "test@mail.com",
        "name": "gchyoo",
        ...
      }
    ```
    // User를 생성
    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto) {}
    ```
    
- ## 4. Spring HATEOAS 기능
  - ## 4-1. 링크 작성 기능
    - 문자열로 링크 생성
    - 컨트롤러와 메서드로 링크 생성
    
  - ## 4-2. 리소스 생성 기능
    - 리소스 : 데이터 + 링크
    
  - ## 4-3. Relation(REL)
    - self : 자기 자신에 대한 URL
    - profile : 현재 응답에 대해 설명된 문서
    - update : 업데이트를 위한 URL
    - query : 조회를 위한 URL
    - ...
    
  - ## 4-4. Deprecated classes
    - ResourceSupport => RepresentationModel
    - Resource => EntityModel
    - Resources => CollectionModel
    - PagedResources => PagedModel    
