package exceptions

const (
	InvalidUserIdFormatExceptionId        = "INVALID_USER_ID"
	InvalidApplicationIdFormatExceptionId = "INVALID_APPLICATION_ID"
	AuthenticationTokenExpiredExceptionId = "EXPIRED_TOKEN"
	AuthenticationTokenInvalidExceptionId = "INVALID_TOKEN"
	InvalidParametersExceptionId          = "INVALID_PARAMETERS"
	ScoreNotFoundExceptionId              = "SCORE_NOT_FOUND"
	UserAlreadyExistsExceptionId          = "USER_ALREADY_EXISTS"
)

type ScoreApplicationException interface {
	GetId() string
	GetMessage() string
}

//------------------------------------------------------------------------------------
type InvalidUserIdFormatException struct {
	message string
}

func NewInvalidUserIdFormatException(message string) *InvalidUserIdFormatException {
	return &InvalidUserIdFormatException{message: message}
}

func (i *InvalidUserIdFormatException) GetId() string {
	return InvalidUserIdFormatExceptionId
}

func (i *InvalidUserIdFormatException) GetMessage() string {
	return i.message
}

//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
type InvalidApplicationIdFormatException struct {
	message string
}

func NewInvalidApplicationIdFormatException(message string) *InvalidApplicationIdFormatException {
	return &InvalidApplicationIdFormatException{message: message}
}

func (i *InvalidApplicationIdFormatException) GetId() string {
	return InvalidApplicationIdFormatExceptionId
}

func (i *InvalidApplicationIdFormatException) GetMessage() string {
	return i.message
}

//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
type AuthenticationTokenExpiredException struct {
	message string
}

func NewAuthenticationTokenExpiredException(message string) *AuthenticationTokenExpiredException {
	return &AuthenticationTokenExpiredException{message: message}
}

func (a *AuthenticationTokenExpiredException) GetId() string {
	return AuthenticationTokenExpiredExceptionId
}

func (a *AuthenticationTokenExpiredException) GetMessage() string {
	return a.message
}

//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
type AuthenticationTokenInvalidException struct {
	message string
}

func NewAuthenticationTokenInvalidException(message string) *AuthenticationTokenInvalidException {
	return &AuthenticationTokenInvalidException{message: message}
}

func (a *AuthenticationTokenInvalidException) GetId() string {
	return AuthenticationTokenInvalidExceptionId
}

func (a *AuthenticationTokenInvalidException) GetMessage() string {
	return a.message
}

//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
type InvalidParametersException struct {
	message string
}

func NewInvalidParametersException(message string) *InvalidParametersException {
	return &InvalidParametersException{message: message}
}

func (i *InvalidParametersException) GetId() string {
	return InvalidParametersExceptionId
}

func (i *InvalidParametersException) GetMessage() string {
	return i.message
}

//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
type ScoreNotFoundException struct {
	message string
}

func NewScoreNotFoundException(message string) *ScoreNotFoundException {
	return &ScoreNotFoundException{message: message}
}

func (s *ScoreNotFoundException) GetId() string {
	return ScoreNotFoundExceptionId
}

func (s *ScoreNotFoundException) GetMessage() string {
	return s.message
}

//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
type UserAlreadyExistsException struct {
	message string
}

func NewUserAlreadyExistsException(message string) *UserAlreadyExistsException {
	return &UserAlreadyExistsException{message: message}
}

func (u *UserAlreadyExistsException) GetId() string {
	return UserAlreadyExistsExceptionId
}

func (u *UserAlreadyExistsException) GetMessage() string {
	return u.message
}

//------------------------------------------------------------------------------------
