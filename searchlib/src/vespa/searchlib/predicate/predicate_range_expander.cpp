// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.


#include "predicate_range_expander.h"

#include <vespa/log/log.h>
LOG_SETUP(".predicate_range_expander");

namespace search {
namespace predicate {

void PredicateRangeExpander::debugLog(const char *fmt, const char *msg) {
    LOG(debug, fmt, msg);
}

}  // namespace predicate
}  // namespace search
