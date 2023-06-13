db.agencies.drop();
db.managers.drop();
db.clients.drop();
db.transactions.drop();
db.accounts.drop();

db.createCollection("accounts", {
    validator: {
        $jsonSchema: {
            bsonType: 'object',
            additionalProperties: false,
            required: ['_id', 'balance', 'created_at', 'closed_at', 'type', 'clients_id', 'agencies_id'],
            properties: {
                _id: {bsonType: 'number'},
                balance: {
                    bsonType: 'double',
                    description: 'value must figure belong these values'
                },
                created_at: {
                    bsonType: [ "null", "string" ],
                    description: "value must be in the format DD/ MM / YYYY"
                },
                closed_at: {
                    bsonType: [ "null", "string" ],
                    description: "value must be in the format DD / MM / YYYY or null"
                },
                type: {
                    'enum': ['current account', 'savings account'],
                    description: 'value must figure belong these values'
                },
                clients_id: {bsonType: 'number'},
                agencies_id: {bsonType: 'number'}
            }
        }
    }
})
db.createCollection("agencies", {
    validator:
        {
            $jsonSchema: {
                bsonType: 'object',
                additionalProperties: false,
                required: [
                    '_id',
                    'name',
                    'address'
                ],
                properties: {
                    _id: {
                        bsonType: 'number'
                    },
                    name: {
                        bsonType: 'string',
                        description: 'must be a string and is required'
                    },
                    address: {
                        bsonType: 'object',
                        required: [
                            'numberStreet',
                            'street',
                            'town',
                            'postalCode',
                            'country'
                        ],
                        properties: {
                            numberStreet: {
                                bsonType: 'number'
                            },
                            street: {
                                bsonType: 'string'
                            },
                            town: {
                                bsonType: 'string'
                            },
                            postalCode: {
                                bsonType: 'string'
                            },
                            country: {
                                bsonType: 'string'
                            }
                        }
                    },
                    managers_id: {
                        bsonType: 'number'
                    },
                    accounts: {
                        bsonType: 'array',
                        items: {
                            bsonType: 'number'
                        }
                    }
                }
            }
        }
})

db.createCollection("clients", {
    validator:
        {
            $jsonSchema: {
                bsonType: 'object',
                additionalProperties: false,
                required: [
                    '_id',
                    'lastName',
                    'firstName',
                    'phone',
                    'address',
                    'accounts',
                    'transactions'
                ],
                properties: {
                    _id: {
                        bsonType: 'number'
                    },
                    lastName: {
                        bsonType: 'string',
                        description: 'value must figure belong these values'
                    },
                    firstName: {
                        bsonType: 'string'
                    },
                    phone: {
                        bsonType: 'string'
                    },
                    address: {
                        bsonType: 'object',
                        required: ['numberStreet', 'street', 'town', 'postalCode', 'country'],
                        properties: {
                            numberStreet: {
                                bsonType: 'string'
                            },
                            street: {
                                bsonType: 'string'
                            },
                            town: {
                                bsonType: 'string'
                            },
                            postalCode: {
                                bsonType: 'string'
                            },
                            country: {
                                bsonType: 'string'
                            }
                        }
                    },
                    accounts: {
                        bsonType: 'array',
                        items: {
                            bsonType: 'number'
                        }
                    },
                    transactions: {
                        bsonType: 'array',
                        items: {
                            bsonType: 'number'
                        }
                    }
                }
            }
        }
})

db.createCollection("managers", {
    validator:
        {
            $jsonSchema: {
                bsonType: 'object',
                required: [
                    '_id',
                    'name'
                ],
                properties: {
                    _id: {
                        bsonType: 'number'
                    },
                    name: {
                        bsonType: 'string',
                        description: 'must be a string and is required'
                    },
                    agencies_id: {
                        bsonType: 'number'
                    }
                }
            }
        }
})

db.createCollection("transactions", {
    validator:
        {
            $jsonSchema: {
                bsonType: 'object',
                additionalProperties: false,
                required: [
                    '_id',
                    'dateTransaction',
                    'status',
                    'amount',
                    'destination_id_client',
                    'clients_id'
                ],
                properties: {
                    _id: {
                        bsonType: 'number'
                    },
                    dateTransaction: {
                        bsonType: 'string',
                        description: "value must be in the format DD / MM / YYYY"
                },
                status: {
                    'enum': [
                        'in progress',
                        'completed'
                    ],
                    description: 'value must figure belong these values'
                },
                amount: {
                    bsonType: 'double'
                },
                destination_id_client: {
                    bsonType: 'number'
                },
                clients_id: {
                    bsonType: 'number'
                }
            }
        }
}
})