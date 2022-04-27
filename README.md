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
  ```
  @TestPropertySource(locations = "classpath:/application-test.yml") // 테스트 프로퍼티 파일 지정
  @WebMvcTest // MockMvc 사용을 위해 설정
  @AutoConfigureMockMvc(addFilters = false) // Spring security filter 비활성화
  @ExtendWith(MockitoExtension.class)
  ```
  
  - ## 2-2. MockMvc
  ```
  mockMvc.perform(post("/api/events/") // 요청 URI
		.contentType(MediaType.APPLICATION_JSON) // 컨텐츠 형식 설정
		.characterEncoding(StandardCharsets.UTF_8) // 문자열 포맷 설정
		.accept(MediaTypes.HAL_JSON) // Hypertext Application Language에 준하는 요청
		.content(objectMapper.writeValueAsString(event))
	)
	.andDo(print()) // 요청과 응답을 출력
	.andExpect(status().isCreated()) // 응답이 201 CREATED인지 확인
	.andExpect(jsonPath("id").exists()); // JSON에 ID가 있는지 확인
  ```
