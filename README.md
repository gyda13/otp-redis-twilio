# otp-redis-twilio

## Features
- Generate and send OTP to a provided phone number via Twilio WhatsApp(or SMS).
- Verify the OTP against the phone number.
- Store OTPs in Redis for validation and expiration management.
- API documentation provided by Swagger UI.


## Configuration
Configure Twilio credentials:

- Open the application.properties file and set your Twilio Account SID and Auth Token
``` properties
twilio.account.sid=your-account-sid
twilio.auth.token=your-auth-token
twilio.phone.number=your-twilio-phone-number
```

Run Redis using Docker
- Run the following command to pull the Redis Docker image and start a Redis container
```
docker run -d -p 6379:6379 --name redis-container redis
```
## API Endpoints
### Send OTP
- URL: POST /otp/send

- Description: Generate and send an OTP to the provided phone number.

- Request Body:
```json
{
  "phoneNumber": "string"
}
```
- Response Body:
```json
{
    "phoneNumber": "string",
    "codeLength": 6,
    "timeToLive": 2
}
```
### Verify OTP
- URL: POST /otp/verify

- Description: Verify the provided OTP against the phone number.

- Request Body:
```json
{
  "phoneNumber": "string",
  "otpCode": "string"
}
```
- Response Body:
```json
{
  "validation": "string"
}
```

