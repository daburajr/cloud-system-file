# variables.tf

variable "key_name" {
  description = "Nome do par de chaves SSH"
  type        = string
  default     = "id_rsa"
}

variable "path_public_key" {
  description = "Caminho absoluto da public key"
  type        = string
  default     = "~/Desktop/id_rsa.pub"
}

variable "region" {
  description = "Região da AWS"
  type        = string
  default     = "us-east-1"
}

variable "ec2_instance_ami" {
  description = "AMI da Instancia EC2"
  type        = string
  default     = "ami-05576a079321f21f8"
}

variable "ec2_instance_type" {
  description = "Instance type do EC2"
  type        = string
  default     = "t2.micro"
}

variable "ec2_instance_name" {
  description = "Instance name do EC2"
  type        = string
  default     = "zup-ec2-instance"
}
