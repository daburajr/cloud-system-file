# variables.tf

variable "aws_region" {
  description = "AWS region for the EFS filesystem"
  type        = string
  default     = "us-east-1"
}

variable "aws_s3_name" {
  description = "AWS S3 name"
  type        = string
  default     = "cloud-system-file"
}