> # 스프링 부트 REST API TDD 연습 프로젝트

- ## 목차
- [1. REST API란 무엇인가?](#1-rest-api란-무엇인가)
  - [1-1. REST API의 조건](#1-1-rest-api의-조건)
  - [1-2. Uniform interface란?](#1-2-uniform-interface란)
  - [1-3. HTTP methods](#1-3-http-methods)
- [2. ControllerTest](#2-controllertest)
  - [2-1. Class annotations](#2-1-class-annotations)
  - [2-2. MockMvc](#2-2-mockmvc)
  - [2-3. BDD](#2-3-bdd)
- [3. Controller](#3-controller)
  - [3-1. Validation](#3-1-validation)
  - [3-2. ResponseEntity](#3-2-responseentity)
  - [3-3. Parameters](#3-3-parameters)
- [4. Spring HATEOAS 기능](#4-spring-hateoas-기능)
  - [4-1. 링크 작성 기능](#4-1-링크-작성-기능)
  - [4-2. 리소스 생성 기능](#4-2-리소스-생성-기능)
  - [4-3. Relation(REL)](#4-3-relationrel)
  - [4-4. Deprecated classes](#4-4-deprecated-classes)
  - [4-5. EntityModel](#4-5-entitymodel)

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
  
  - ## 1-3. HTTP methods
    - GET     : 데이터 조회(CRUD의 **R**etrieve)
    - POST    : 데이터 생성(CRUD의 **C**reate)
    - PUT     : 데이터 **전체** 수정, 없으면 생성(CRUD의 **U**pdate + **C**reate)
    - PATCH   : 데이터 **일부** 수정(CRUD의 **U**pdate)
    - DELETE  : 데이터 삭제(CRUD의 **D**elete)
    - HEAD    : 응답 헤더 확인(GET에서 Body를 제외한 응답 헤더만 응답으로 받음)
    - OPTIONS : 지원하는 요청 확인(지원하는 HTTP 메서드 타입을 응답으로 받음)
    - TRACE   : 웹 서버까지의 네트워크 경로 확인(Web proxy, Web cache 서버의 요청 메시지를 응답으로 받음)
  
- ## 2. ControllerTest
  - ## 2-1. Class annotations
    - Controller 테스트의 경우 mocking 해야할 범위가 넓어지기 때문에, @SpringBootTest를 사용
  
  - ## 2-2. MockMvc
    - Controller의 API를 테스트하기 위해 의존성을 주입하여 테스트
    - andExpect(ResultMatcher)를 사용하여 header, json을 검증
    
  - ## 2-3. BDD
    > ### Given
      - DB가 연관된 테스트라면 repository에 데이터 입력
      ```
      userRepository.save(userEntity);
      ```
      
    > ### When
      - mockMvc를 통한 Request
      ```
      mockMvc.perform([methodType]([URI]/{pathVariable}, [var])
      ```
      
    > ### Then
      - mockMvc에서 받은 Response
      ```
      // When
      mockMvc.perform(...)
      
      // Then
          .andDo(print()) // 요청/응답 출력
          
          // Header
          .andExpect(status().isOk()) // HttpStatus가 200 OK 인지
          .andExpect(header().exists(HttpHeaders.LOCATION)) // HttpHeaders.LOCATION 헤더가 존재하는지
          .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) // Content-Type가 application/hal+json 인지
          
          // Body
          .andExpect(jsonPath("path.of.json").exists())  // Key가 존재하는지
          .andExpect(jsonPath("path.of.json").value())   // Value가 일치하는지
      ```
    
- ## 3. Controller
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
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") int id){}
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
  
  - ## 4-5. EntityModel
    - 링크 생성을 위해 EntityModel을 상속받은 CustomEntityModel
    ```
    public class CustomEntityModel extends EntityModel<CustomEntityClass> {
      public CustomEntityModel(CustomEntityClass customEntityClass) {
        super(customEntityClass);
        
        // 생성자에서 http://domain/api/custom/{id} 링크를 생성(self relation)
        add(WebMvcLinkBuilder.linkTo(CustomController.class) // linkTo는 Controller 클래스의 @RequestMapping("/api/custom")으로 지정된 URI를 반환
            .slash(customEntityClass.getId()) // slash는 '/'와 파라미터로 주어진 값을 표시
            .withSelfRel() // "_links"의 "self" key에 들어가도록 함
        );
      }
    }
    ```
    - CustomEntityModel 인스턴스에 링크를 추가
    ```
    // CustomEntityModel 인스턴스 생성
    CustomEntityModel customEntityModel = new CustomEntityModel(customEntityClass);
    
    // http://domain/api/custom 링크 추가
    customEntityModel.add(WebMvcLinkBuilder.linkTo(CustomController.class)
        .withRel("query-customs"));
    customEntityModel.add(WebMvcLinkBuilder.linkTo(CustomController.class)
        .withRel("update-custom"));
    ```
    
    - 응답 Body의 links
    ```
    {
      ...
      "_links": {
          "self": {
              "href": "http://domain/api/custom/1"
          },
          "query-customs": {
              "href": "http://localhost/api/custom"
          },
          "update-custom": {
              "href": "http://localhost/api/custom"
          }
      }
    }
    ```
