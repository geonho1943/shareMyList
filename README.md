# SML (ShareMyList)

SML은 유튜브 영상 링크를 공유할 수 있는 웹사이트입니다.<br>
유저는 플레이리스트를 생성하여 관련 영상을 분류,<br>
유튜브 영상의 링크를 공유 할수 있습니다.

## 배포 및 데모
- [SML 데모 - https://sharemylist.kro.kr](https://sharemylist.kro.kr)

## 기간
- 2023 10/20 ~ 2024 05/31

## 기능
- 회원가입 및 로그인
- 유튜브 링크 공유
- 플레이리스트 생성 및 관리
- 회원 탈퇴 시 생성물 비활성화
- 관리자 대시보드에서 서비스사용 통계 그래프 제공 (예정)

## 사용한 기술
- **백엔드**: Java-17, Spring Boot-3.1.4, Spring Data JPA
- **프론트엔드**: Thymeleaf, Bootstrap-5.3.2
- **데이터베이스**: MariaDB-10.11.7
- **빌드**: Gradle
- **호스팅**: 오라클 클라우드 (OCI)

## 디랙터리 구조
각 레이어는 도메인을 기준으로 분리,<br>
컨트롤러,jpa 리포지터리, 서비스로직 으로 이루어져있으며<br>
컨트롤러레이어와 서비스레이어간의 데이터이동을 위한 dto,<br>
jpa메서드와 서비스간의 엔티티 클래스로 구성되어있습니다.
```
ShareMyList/src/main/
├── java/
│   └── com/
│       └── geonho1943/
│           └── sharemylist/
│               ├── controller/
│               │   ├── CardController
│               │   ├── PlaylistController
│               │   └── UserController
│               ├── dto/
│               │   ├── CardDto
│               │   ├── PlaylistDto
│               │   └── UserDto
│               ├── model/
│               │   ├── Card
│               │   ├── EventLog
│               │   ├── Playlist
│               │   └── User
│               ├── repository/
│               │   ├── CardRepository
│               │   ├── EventLogRepository
│               │   ├── PlaylistRepository
│               │   └── UserRepository
│               ├── service/
│               │   ├── CardService
│               │   ├── PlaylistService
│               │   ├── RecordService
│               │   └── UserService
│               └── SharemylistApplication
└── resources/
    ├── templates/
    │   ├── card/
    │   │   └── cardinfo
    │   ├── fragments/
    │   │   └── navigationbar
    │   ├── playlist/
    │   │   ├── createplaylist
    │   │   ├── linkupload
    │   │   ├── playlist
    │   │   └── playlistinfo
    │   ├── user/
    │   │   ├── userjoin
    │   │   ├── userlogin
    │   │   └── userresign
    │   └── home
    └── application-mariaDB.properties
```

## userFlow
![SML_userflow](https://github.com/geonho1943/shareMyList/assets/106109077/ceb14aee-b928-4e37-905b-aa2ee0f17cc9)

## ERD
![SML_ERD](https://github.com/geonho1943/shareMyList/assets/106109077/974b50a2-3c63-410a-801c-2b8c61363359)

## API Document

### User
| HTTP Method | Endpoint  | Description | Request Parameters   | Response                                                                                              |
|-------------|-----------|-------------|----------------------|-------------------------------------------------------------------------------------------------------|
| GET         | /login    | 로그인 페이지 반환  | 없음                   | user/userlogin                                                                                        |
| POST        | /login    | 로그인 요청 처리   | `UserDto loginInfo`  | 성공 시: redirect:/ <br> 실패 시: user/userlogin + `error`                             |
| GET         | /logout   | 로그아웃 처리     | 없음                   | redirect:/                                                                                            |
| GET         | /resign   | 회원 탈퇴 페이지 반환 | 없음                   | user/userresign                                                                                       |
| POST        | /resign   | 회원 탈퇴 요청 처리 | `UserDto resignInfo` | 성공 시: user/userlogin + `success` <br> 실패 시: user/userlogin + `error`            |
| GET         | /join     | 회원 가입 페이지 반환 | 없음                   | user/userJoin                                                                                         |
| POST        | /join     | 회원 가입 요청 처리 | `UserDto joinInfo`   | 성공 시: user/userlogin + `success` <br> 실패 시: user/userJoin + `error` |

### Card
| HTTP Method | Endpoint  | Description | Request Parameters | Response                                               |
|-------------|-----------|------------|------------------|--------------------------------------------------------|
| GET         | /                                 | 홈페이지 반환   | 없음                                     | home + `cardList`                                        |
| GET         | /search                           | 검색결과 페이지 반환 | `String keyword`                         | home + `cardList or emptyData`                         |
| GET         | /linkupload                       | 링크 업로드 페이지 반환 | 없음                                     | playlist/linkupload + `userPlayList` or user/userlogin   |
| POST        | /submitYoutubeLink                | 링크의 메타데이터 저장 | `String youtubeLink`, `int playlistIdx`  | 성공시: redirect:/ <br> 실패 시: playlist/linkupload + `error` |
| GET         | /cardInfo/{cardIdx}               | 카드 정보 반환 | `int cardIdx`                            | card/cardinfo + `cardInfo` or user/userlogin             |
| DELETE      | /deleteCard/{cardPlaylistIdx}/{cardIdx} | 카드 삭제   | `int cardPlaylistIdx`, `int cardIdx`     | 성공시: redirect:/ <br> 실패시: playlist/playlistinfo + `error` |

### Playlist
| HTTP Method | Endpoint                         | Description    | Request Parameters       | Response                                             |                                                            
|-------------|----------------------------------|----------------|--------------------------|------------------------------------------------------|
| GET         | /playlist                        | 사용자 플레이리스트 페이지 반환 | 없음                     | playlist/playlist + `userPlayList` or user/userlogin |
| GET         | /createplaylist                  | 플레이리스트 생성 페이지 반환 | 없음                     | playlist/createplaylist or user/userlogin            |
| POST        | /createplaylist                  | 플레이리스트 생성      | `String playlistName`    | playlist or user/userlogin                           |
| GET         | /playlistInfo/{playlistIdx}      | 특정 플레이리스트 정보 반환 | `int playlistIdx`        | playlist/playlistinfo + `cardInfoList or emptyData`  |
| DELETE      | /deletePlaylist/{playlistIdx}    | 특정 플레이리스트 삭제   | `int playlistIdx`        | 성공시: redirect:/ <br> 실패 시: playlist + `error`        |



## 실행
### 환경설정
application-mariaDB.properties에 데이터베이스 커넥션정보를 기입해야 합니다
```
spring.datasource.url=jdbc:mariadb://'db.server.host.ip':'port'/share_my_list
spring.datasource.username=root
spring.datasource.password='passwd'
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
```
### 설치
```
$ git@github.com:geonho1943/shareMyList.git
$ cd shamrmylist
$ ./gradlew build
$ java -jar sharemylist*.jar
```

## License
- [MIT License](https://github.com/geonho1943/shareMyList?tab=MIT-1-ov-file)
