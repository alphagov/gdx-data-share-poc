resource "aws_cloudfront_distribution" "gdx_data_share_poc" {
  provider = aws.us-east-1

  origin {
    domain_name = aws_lb.load_balancer.dns_name
    origin_id   = "${var.environment}-gdx-data-share-poc-lb"
  }

  enabled         = true
  is_ipv6_enabled = false

  default_cache_behavior {
    allowed_methods  = ["GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = "${var.environment}-gdx-data-share-poc-lb"

    forwarded_values {
      query_string = true
      headers      = ["*"]
      cookies {
        forward = "all"
      }
    }

    # Auto-compress stuff if given Accept-Encoding: gzip header
    compress = true

    viewer_protocol_policy = "redirect-to-https"
    min_ttl                = 0
    default_ttl            = 3600
    max_ttl                = 86400
  }

  # This is the lowest class and only offers nodes in Europe, US and Asia.
  # Given that most of our traffic is from the UK, this should be sufficient.
  price_class = "PriceClass_100"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  web_acl_id = aws_wafv2_web_acl.gdx_data_share_poc.arn
}