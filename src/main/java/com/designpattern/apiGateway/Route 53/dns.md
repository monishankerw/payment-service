## Route 53 / DNS in your AWS architecture

![Image](https://images.openai.com/static-rsc-4/hBpa7ejzYsIAlsPUKX1YjgQnEmN8IAhlS_9pSTPkDgyFJlxDCqXYKX30E0z4gkDdJdAv11RuGIy_QGbm7Hn8DoBCApGSgPiB_97s1CHRaXSYvk8ipuZ1CD8AfQA46IySdDmQdNRdIA4aACyyD7888cd3fXiX5_rns6ucdfJ-Dksxk7iAUnm1xSTGi3mgqsvQ?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/Asmc1-O_1fbF5DahhtCxXU8IGYjUrjb-WOnQHHxIFIYR_9Bi-qfHBMwrLvfmopF7Poumpt4SyKlPJp92Md3AGi8drXhvuI04vsObFg6myU9U3uFhzH3XTZjJGAM8vs7YFHTrzupkdWCArO9x4wdiyBll55NYqKSnFugyzeg_LcNQAAtNn8h5cjyCTrvkPov1?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/eW9dutLaYq-K_SxvfLryy_FiiKHKL_TzlRDswXuMqhJVQwSSIO21vMyEEjrNI2s2ZBpwbsj8hB46Ysqjdp6UkF_8nSAOLy-iUUgPLtQyO5CIamxPnCUTL6T6WTaIvbZRamcxkTStgTt_kaB3A1zBC0Ow3kj1UHwiI6FvWBU1J1LJ9P_r3ZRIiEfI-kDEJI5r?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/3HPLiFnpFm8FFQYL8OkfvLvTkgmjYOPgckTqQK1XWNAPW6oSrguj1ZndBrm0CibBjuMuQonRrQrpcG1Rlp6Q2F6H80DK5te4OHpDbEiKx8xBOKK5C1EvUE6SnMFdvcLhXbnRr_BTNhV7_ktYOjFXlgtiXqYc-EDKQMdacM6yyspHhsRF2Tf_BBSl1BKELgZs?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/G4VYFH2S_yaiVkRcBXxbDl8lxnOjQM2cj7fGMZMPshnzgi2Ljl042UKh2jhBUL8uH-v5XBhQRFApDtSfRWhthFg_meUtONc8wfMHK4rkWZw0_bBlHSA1s2-t73hLEzjdZDXVzW6X9A7oHYKEvaRx_adm9A0nIEF8jFloEsE-a5TXjRCzN737tgRf3JIo1lil?purpose=fullsize)

Think of **Route 53 as the phonebook of your application**.

Instead of users calling:

```text
https://52.xx.xx.xx
```

they call:

```text
https://api.mycompany.com
```

The flow is:

```text
Mobile / Web
      |
      | https://api.mycompany.com
      ↓
   Route 53
      |
      | DNS lookup
      ↓
Application Load Balancer
      |
      ↓
API Gateway
      |
      ↓
Microservices
```

### 1. Buy/register domain

Example:

```text
mycompany.com
```

You can register it through Route 53 or use a domain registered elsewhere.

### 2. Create hosted zone

AWS Console:

```text
Route 53
   ↓
Hosted zones
   ↓
Create hosted zone
```

Domain:

```text
mycompany.com
```

Route 53 creates DNS records such as:

```text
NS
SOA
```

If your domain is registered elsewhere, configure the domain's nameservers to the Route 53 nameservers.

### 3. Create API DNS record

Create:

```text
api.mycompany.com
```

Record:

```text
Type: A
Alias: Yes
Target: Application Load Balancer
```

So:

```text
api.mycompany.com
        ↓
Route 53
        ↓
ALB
        ↓
ECS API Gateway
```

You generally **do not put the ALB IP address directly into DNS**; use an AWS Alias record pointing to the ALB.

### 4. HTTPS

Use:

```text
Route 53
   ↓
ACM Certificate
   ↓
ALB :443
   ↓
API Gateway
```

Request an ACM certificate for:

```text
api.mycompany.com
```

Attach it to the ALB HTTPS listener.

Then your application URL becomes:

```text
https://api.mycompany.com/api/payments
```

### Interview answer

> **"We use Route 53 for DNS. Instead of exposing the ALB address to clients, we create an A/AAAA Alias record such as `api.company.com` pointing to the Application Load Balancer. The ALB terminates HTTPS using an ACM certificate and forwards requests to the API Gateway ECS service."**

So remember:

```text
Domain
  ↓
Route 53
  ↓
ALB
  ↓
API Gateway
  ↓
Microservices
```
